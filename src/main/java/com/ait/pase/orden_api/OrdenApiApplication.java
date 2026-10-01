package com.ait.pase.orden_api;

import com.ait.pase.orden_api.entity.Genero;
import com.ait.pase.orden_api.entity.Usuario;
import com.ait.pase.orden_api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class OrdenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdenApiApplication.class, args);
	}
	@Bean
    CommandLineRunner runner(
			UsuarioRepository usuarioRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			if(usuarioRepository.count()==0) {
				String nombre = "Usuario";
				String apellido = "Uno";
				String email = nombre.toLowerCase() + "." + apellido.toLowerCase() + "@ait.pase.com";
				Usuario usuario = Usuario.builder()
						.nombre(nombre + " " + apellido)
						.email(email)
						.password(passwordEncoder.encode("password"))
						.edad(37)
						.genero(Genero.MASCULINO)
						.build();
				usuarioRepository.save(usuario);
			}
		};
	}
}
