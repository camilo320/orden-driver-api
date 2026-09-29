package com.ait.pase.orden_api.repository;

import com.ait.pase.orden_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository  extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
}
