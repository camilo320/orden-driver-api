package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.entity.Driver;
import com.ait.pase.orden_api.exception.OperationNotPermittedException;
import com.ait.pase.orden_api.model.OrdenResponse;
import com.ait.pase.orden_api.entity.Orden;
import com.ait.pase.orden_api.entity.Status;
import com.ait.pase.orden_api.exception.ResourceNotFoundException;
import com.ait.pase.orden_api.model.OrdenRequest;
import com.ait.pase.orden_api.repository.DriverRepository;
import com.ait.pase.orden_api.repository.OrdenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrdenService {

    private final OrdenRepository repository;
    private final DriverRepository driverRepository;
    private final OrdenMapper mapper;
    private final FileStorageService fileStorageService;

    public UUID save(OrdenRequest ordenRequest){
        Orden orden = Orden.from(ordenRequest);
        orden.setStatus(Status.CREATED);
        return repository.save(orden).getId();
    }
    public List<OrdenResponse> getAll() {
       return ((List<Orden>) repository.findAll())
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenResponse> findByStatus(Status status) {
        return repository.findByStatus(status)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenResponse> findByOrigin(String origin) {
        return repository.findByOrigin(origin)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenResponse> findByDestination(String destination) {
        return repository.findByDestination(destination)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenResponse> findByCreatedAtBetween(LocalDateTime to, LocalDateTime from) {
        return repository.findByCreatedAtBetween(to, from)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public OrdenResponse getById(UUID id) {
        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id : " + id));
    }
    public UUID changeStatus(UUID id,Status nuevoStatus) {
        Orden orden = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con el id :: " + id));

        if(orden.getStatus() == nuevoStatus){
            throw new OperationNotPermittedException("Ya se encuentra en el mismo status");
        }
        if(orden.getStatus() == Status.DELIVERED){
            throw new OperationNotPermittedException("La orden ya esta entregada, ya no se puede cambiar su status");
        }
        if(orden.getStatus() == Status.CANCELLED){
            throw new OperationNotPermittedException("La orden ya esta cancelada, ya no se puede cambiar su status");
        }

        switch (orden.getStatus()){
            case CREATED:
                if(nuevoStatus == Status.IN_TRANSIT || nuevoStatus == Status.CANCELLED){
                    orden.setStatus(nuevoStatus);
                }else{
                    throw new OperationNotPermittedException("Del status CREATED solo se puede cambiar a IN_TRANSIT o CANCELLED");
                }
                break;
            case IN_TRANSIT:
                if(nuevoStatus == Status.DELIVERED || nuevoStatus == Status.CANCELLED){
                    orden.setStatus(nuevoStatus);
                }else{
                    throw new OperationNotPermittedException("Del status IN_TRANSIT solo se puede cambiar a DELIVERED o CANCELLED");
                }
                break;
        }
        repository.save(orden);
        return orden.getId();
    }

    public void deleteAll() {
        repository.deleteAll();
    }
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }


    public void asignacion(UUID ordenId, UUID driverId, MultipartFile pdf, MultipartFile imagen) {
        Orden orden = repository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID:: " + ordenId));

        if(orden.getStatus() != Status.CREATED){
            throw new OperationNotPermittedException("La asignacion solo esta permitido cuando la orden tiene status CREATED ");
        }
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver no encontrada con ID:: " + driverId));

        if( !driver.getActive() ){
            throw new OperationNotPermittedException("La asignacion solo esta permitido cuando el driver esta activo");
        }

        orden.setAsignacionArchivo(fileStorageService.save(pdf, ordenId.toString()));
        orden.setAsignacionImagen(fileStorageService.save(imagen,ordenId.toString()));
        orden.setDriver(Driver.builder().id(driverId).build());
        repository.save(orden);
    }
}
