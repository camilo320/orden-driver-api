package com.ait.pase.orden_api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DriverRequest(
        @NotNull(message = "No puede ser nulo")
        @NotEmpty(message = "No puede estar vacio")
        String name,
        @NotNull(message = "No puede ser nulo")
        @NotEmpty(message = "No puede estar vacio")
        String licenseNumber
) {
}
