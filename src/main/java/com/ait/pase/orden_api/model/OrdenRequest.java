package com.ait.pase.orden_api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrdenRequest(
        @NotNull(message = "No puede ser nulo")
        @NotEmpty(message = "No puede estar vacio")
        String origin,
        @NotNull(message = "No nulo")
        @NotEmpty(message = "No vacio")
        String destination
) {
}
