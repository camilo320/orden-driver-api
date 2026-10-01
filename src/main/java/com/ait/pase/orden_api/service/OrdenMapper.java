package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.entity.Orden;
import com.ait.pase.orden_api.model.OrdenResponse;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrdenMapper implements Function<Orden, OrdenResponse> {
    @Override
    public OrdenResponse apply(Orden orden) {
        return OrdenResponse.builder()
                .id(orden.getId())
                .origin(orden.getOrigin())
                .destination(orden.getDestination())
                .createdAt(orden.getCreatedAt())
                .updatedAt(orden.getUpdatedAt())
                .status(orden.getStatus())
                .driver(orden.getDriver() ==null ? null:orden.getDriver().toResponse())
                .build();
    }
}
