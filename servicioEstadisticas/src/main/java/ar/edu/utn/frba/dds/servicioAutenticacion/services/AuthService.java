package ar.edu.utn.frba.dds.servicioAutenticacion.services;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Usuario;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.UsuariosRepository;
import ar.edu.utn.frba.dds.servicioAutenticacion.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByUsernameIgnoreCase(username);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario" + username + " no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new NotFoundException("Usuario" + username + " no encontrado");
        }

        return usuario;
    }

    public String generarAccessToken(Usuario usuario) {
        return JwtUtil.generarAccessToken(usuario);
    }

    public String generarRefreshToken(Usuario usuario) {
        return JwtUtil.generarRefreshToken(usuario);
    }

    public Usuario obtenerUsuario(String username) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByUsernameIgnoreCase(username);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario" + username + " no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        return usuario;
    }
}
