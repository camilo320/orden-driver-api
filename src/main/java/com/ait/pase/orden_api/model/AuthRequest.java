package com.ait.pase.orden_api.model;

public record AuthRequest(
        String username,
        String password
) {
}
