package ar.edu.utn.frba.dds.servicioAutenticacion.services;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions.AlreadyExistent;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions.CamposInvalidosError;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions.CamposInvalidosError;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Usuario;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories.UsuariosRepository;
import ar.edu.utn.frba.dds.servicioAutenticacion.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
            throw new CamposInvalidosError("Usuario o contraseña incorrectos, revise los campos e intente de nuevo.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new CamposInvalidosError("Usuario o contraseña incorrectos, revise los campos e intente de nuevo.");
        }

        return usuario;
    }

    public Usuario registrarUsuario(Usuario registerUserDTO) {
        Optional<Usuario> usuarioExistente = usuariosRepository.findByUsernameIgnoreCase(registerUserDTO.getUsername());
        if (usuarioExistente.isPresent()) {
            throw new AlreadyExistent("El nombre de usuario ya existe. Por favor intente de nuevo con otro.");
        }

        String secret = passwordEncoder.encode(registerUserDTO.getPassword());
        registerUserDTO.setPassword(secret);

        Usuario usuarioGuardado = usuariosRepository.save(registerUserDTO);

        return usuarioGuardado;
    }

    public Usuario modificarUsuario(Long id, Usuario updateUserDTO) {
        Usuario usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new CamposInvalidosError("Usuario no encontrado"));

        if (!usuario.getUsername().equalsIgnoreCase(updateUserDTO.getUsername())) {
            Optional<Usuario> usuarioConNuevoUsername = usuariosRepository.findByUsernameIgnoreCase(updateUserDTO.getUsername());
            if (usuarioConNuevoUsername.isPresent()) {
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
        }

        usuario.setUsername(updateUserDTO.getUsername());

        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        usuario.setApellido(updateUserDTO.getApellido());
        usuario.setNombre(updateUserDTO.getNombre());
        usuario.setFechaNacimiento(updateUserDTO.getFechaNacimiento());
        usuario.setRoles(updateUserDTO.getRoles());
        usuario.setPermisos(updateUserDTO.getPermisos());

        Usuario usuarioActualizado = usuariosRepository.save(usuario);

        return usuarioActualizado;
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
            throw new CamposInvalidosError("Usuario " + username + " no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        return usuario;
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = usuariosRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new CamposInvalidosError("Usuarios no encontrados");
        }

        return usuarios;
    }
}
