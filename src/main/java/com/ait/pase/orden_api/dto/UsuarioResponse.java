package com.ait.pase.orden_api.dto;

import com.ait.pase.orden_api.entity.Genero;

import java.util.List;
import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nombre,
        String email,
        Genero genero,
        Integer edad,
        List<String> roles,
        String username
) {
}
