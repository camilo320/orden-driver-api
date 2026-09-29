package com.ait.pase.orden_api.model;

public record AuthResponse(
        String token,
        UsuarioDTO usuarioDTO) {
}
