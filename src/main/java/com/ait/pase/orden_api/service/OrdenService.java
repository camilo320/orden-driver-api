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

import static net.logstash.logback.argument.StructuredArguments.kv;

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
        log.info("Guardando Orden {} {}",
                kv("origin", ordenRequest.origin()),
                kv("destination",ordenRequest.destination()));
        Orden orden = Orden.from(ordenRequest);
        orden.setStatus(Status.CREATED);
        return repository.save(orden).getId();
    }
    public List<OrdenResponse> getAll() {
        log.info("Obteniendo todas las ordenes");
       return ((List<Orden>) repository.findAll())
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenResponse> findByStatus(Status status) {
        log.info("Obteniendo ordenes con {}",
                kv("status", status));
        return repository.findByStatus(status)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenResponse> findByOrigin(String origin) {
        log.info("Obteniendo ordenes con {}",
                kv("origin", origin));
        return repository.findByOrigin(origin)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenResponse> findByDestination(String destination) {
        log.info("Obteniendo ordenes con {}",
                kv("destination", destination));
        return repository.findByDestination(destination)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenResponse> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to) {
        log.info("Obteniendo ordenes con fecha {} {}",
                kv("desde", from),
                kv("hasta",to));
        return repository.findByCreatedAtBetween(from, to)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public OrdenResponse getById(UUID id) {
        log.info("Obteniendo orden con {}",
                kv("Id", id));
        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id : " + id));
    }
    public UUID changeStatus(UUID id,Status nuevoStatus) {
        log.info("Cambiando status del orden {} {}",
                kv("Id", id),
                kv("Status",nuevoStatus));
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
        log.warn("Borrando todas las ordenes");
        repository.deleteAll();
    }
    public void deleteById(UUID id) {
        log.warn("Borrando orden con {}",kv("Id",id));
        repository.deleteById(id);
    }


    public void asignacion(UUID ordenId, UUID driverId, MultipartFile pdf, MultipartFile imagen) {
        log.info("Asignacion de un driver a una orden {} {}",
                kv("ordenId",ordenId),
                kv("driverId",driverId));
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
