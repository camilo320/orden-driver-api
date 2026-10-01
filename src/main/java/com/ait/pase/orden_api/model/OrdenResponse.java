package com.ait.pase.orden_api.model;

import com.ait.pase.orden_api.entity.Status;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrdenResponse(
        UUID id,
        String origin,
        String destination,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        DriverResponse driver
) {
}
