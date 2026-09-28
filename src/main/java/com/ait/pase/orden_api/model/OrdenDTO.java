package com.ait.pase.orden_api.model;

import com.ait.pase.orden_api.entity.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrdenDTO(
        UUID id,
        @NotNull(message = "No puede ser nulo")
        @NotEmpty(message = "No puede estar vacio")
        String origin,
        @NotNull(message = "No nulo")
        @NotEmpty(message = "No vacio")
        String destination,
        Status status
) {
}
