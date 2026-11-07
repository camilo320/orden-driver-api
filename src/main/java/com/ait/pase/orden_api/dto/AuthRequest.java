package com.ait.pase.orden_api.dto;

public record AuthRequest(
        String username,
        String password
) {
}
