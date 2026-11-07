package com.ait.pase.orden_api.handler;

import lombok.Builder;

@Builder
public record ExceptionResponse(
        String errorCode,
        String error,
        String descripcion
) {
}
