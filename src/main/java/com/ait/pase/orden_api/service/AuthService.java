package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.entity.Usuario;
import com.ait.pase.orden_api.jwt.JWTUtil;
import com.ait.pase.orden_api.model.AuthRequest;
import com.ait.pase.orden_api.model.AuthResponse;
import com.ait.pase.orden_api.model.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper usuarioMapper;
    private final JWTUtil jwtUtil;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        Usuario principal = (Usuario) authentication.getPrincipal();
        UsuarioDTO usuario = usuarioMapper.apply(principal);
        String token = jwtUtil.issueToken(usuario.username(), usuario.roles());
        return new AuthResponse(token, usuario);
    }

}
