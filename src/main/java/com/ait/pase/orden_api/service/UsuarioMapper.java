package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.entity.Usuario;
import com.ait.pase.orden_api.model.UsuarioDTO;
import org.springframework.stereotype.Service;


import org.springframework.security.core.GrantedAuthority;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UsuarioMapper implements Function<Usuario, UsuarioDTO> {
    @Override
    public UsuarioDTO apply(Usuario us) {
        return new UsuarioDTO(
                us.getId(),
                us.getNombre(),
                us.getEmail(),
                us.getGenero(),
                us.getEdad(),
                us.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                us.getUsername()
        );
    }
}
