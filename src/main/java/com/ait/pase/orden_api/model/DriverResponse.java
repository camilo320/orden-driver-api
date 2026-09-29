package com.ait.pase.orden_api.model;

import lombok.Builder;

import java.util.UUID;
@Builder
public record DriverResponse(
        UUID id,
        String name,
        String licenseNumber,
        Boolean active
) {
}
