package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.model.OrdenDTO;
import com.ait.pase.orden_api.entity.Orden;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrdenMapper implements Function<Orden, OrdenDTO> {
    @Override
    public OrdenDTO apply(Orden orden) {
        return new OrdenDTO(
                orden.getId(),
                orden.getOrigin(),
                orden.getDestination(),
                orden.getStatus()
        );
    }
}
