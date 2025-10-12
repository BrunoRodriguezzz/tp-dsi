package ar.edu.utn.frba.dds.servicioAutenticacion.controllers;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.dto.AccessRequest;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.dto.AuthResponseDTO;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.dto.RefreshRequest;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.dto.TokenResponse;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions.NotFoundError;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.exceptions.RegisterError;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Usuario;
import ar.edu.utn.frba.dds.servicioAutenticacion.services.AuthService;
import ar.edu.utn.frba.dds.servicioAutenticacion.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponseDTO> loginApi(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            // Validación básica de credenciales
            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Autenticar usuario usando el AuthService
            Usuario usuario = authService.autenticarUsuario(username, password);

            // Generar tokens
            String accessToken = authService.generarAccessToken(usuario);
            String refreshToken = authService.generarRefreshToken(usuario);

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            log.info("El usuario {} está logueado. El token generado es {}", username, accessToken);

            return ResponseEntity.ok(response);
        } catch (NotFoundError e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        try {
            String username = JwtUtil.validarToken(request.getRefreshToken());

            // Validar que el token sea de tipo refresh
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getKey())
                    .build()
                    .parseClaimsJws(request.getRefreshToken())
                    .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.badRequest().build();
            }
            Usuario usuario = authService.obtenerUsuario(username);
            String newAccessToken = JwtUtil.generarAccessToken(usuario);
            TokenResponse response = new TokenResponse(newAccessToken, request.getRefreshToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Usuario> getUsuario(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String accessToken = authHeader.substring(7);
            String username = JwtUtil.validarToken(accessToken);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getKey())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            Usuario usuario = authService.obtenerUsuario(claims.get("username").toString());
            return ResponseEntity.ok(usuario);
        } catch (NotFoundError e) {
            log.error("Usuario no encontrado", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener roles y permisos del usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }

    // TODO: Revisar el parametro
    @GetMapping("/users")
    public ResponseEntity<List<Usuario>> getUsuarios(Authentication authentication) {
        try {
            List<Usuario> response = authService.obtenerUsuarios();
            return ResponseEntity.ok(response);
        } catch (NotFoundError e) {
            log.error("Usuarios no encontrado", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener roles y permisos del usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registrarUsuario(@Valid @RequestBody Usuario usuario) {
            // Validación básica de credenciales
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty() ||
                    usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                throw new RegisterError("El nombre y la contraseña no pueden estar vacíos");
            }

            Usuario usuarioCreado = authService.registrarUsuario(usuario);

            String accessToken = authService.generarAccessToken(usuarioCreado);
            String refreshToken = authService.generarRefreshToken(usuarioCreado);

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            log.info("El usuario {} está logueado. El token generado es {}", usuarioCreado.getUsername(), accessToken);

            return ResponseEntity.ok(response);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Usuario> modificarUsuario(@PathVariable("id") Long id, @Valid @RequestBody Usuario usuario) {
        Usuario usuarioModificado = authService.modificarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioModificado);
    }
}
