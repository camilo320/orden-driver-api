package com.ait.pase.orden_api.model;

import lombok.Builder;

@Builder
public record ExceptionResponse(
        String error,
        String descripcion
) {
}
