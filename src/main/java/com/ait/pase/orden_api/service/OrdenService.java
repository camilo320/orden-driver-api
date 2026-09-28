package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.exception.OperationNotPermittedException;
import com.ait.pase.orden_api.model.OrdenDTO;
import com.ait.pase.orden_api.entity.Orden;
import com.ait.pase.orden_api.entity.Status;
import com.ait.pase.orden_api.exception.ResourceNotFoundException;
import com.ait.pase.orden_api.repository.OrdenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final OrdenMapper mapper;

    public UUID save(Orden orden) {
        return repository.save(orden).getId();
    }
    public UUID save( OrdenDTO dto) {
        Orden orden = Orden.fromCreate(dto);
        orden.setStatus(Status.CREATED);
        return repository.save(orden).getId();
    }
    public List<OrdenDTO> getAll() {
       return ((List<Orden>) repository.findAll())
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenDTO> findByStatus(Status status) {
        return repository.findByStatus(status)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenDTO> findByOrigin(String origin) {
        return repository.findByOrigin(origin)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenDTO> findByDestination(String destination) {
        return repository.findByDestination(destination)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public List<OrdenDTO> findByCreatedAtBetween(LocalDateTime to, LocalDateTime from) {
        return repository.findByCreatedAtBetween(to, from)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    public OrdenDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ResourceNotFoundException("orden con id : " + id));
    }
    public UUID changeStatus(UUID id,Status nuevoStatus) {
        Orden orden = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada con el id :: " + id));

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


}
