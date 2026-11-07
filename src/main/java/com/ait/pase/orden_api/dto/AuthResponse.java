package com.ait.pase.orden_api.dto;

public record AuthResponse(
        String token,
        UsuarioResponse usuarioResponse) {
}
