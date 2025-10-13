package ar.edu.utn.frba.dds.client.providers;

import ar.edu.utn.frba.dds.client.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.client.dtos.auth.UsuarioDTO;
import ar.edu.utn.frba.dds.client.services.AuthApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final AuthApiService externalAuthService;

    public CustomAuthProvider(AuthApiService externalAuthService) {
        this.externalAuthService = externalAuthService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            // Llamada a servicio externo para obtener tokens
            AuthResponseDTO authResponse = externalAuthService.login(username, password);

            if (authResponse == null) {
                throw new BadCredentialsException("Usuario o contraseña inválidos");
            }

            log.info("Usuario logeado! Configurando variables de sesión");
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            request.getSession().setAttribute("accessToken", authResponse.getAccessToken());
            request.getSession().setAttribute("refreshToken", authResponse.getRefreshToken());
            request.getSession().setAttribute("username", username);

            log.info("Buscando información del usuario");
            UsuarioDTO usuario = externalAuthService.getUsuario(authResponse.getAccessToken());

            request.getSession().setAttribute("id", usuario.getId());

            log.info("Cargando roles y permisos del usuario en sesión");
            request.getSession().setAttribute("roles", usuario.getRoles());
            request.getSession().setAttribute("permisos", usuario.getPermisos());
            request.getSession().setAttribute("nombre", usuario.getNombre());
            request.getSession().setAttribute("apellido", usuario.getApellido());
            request.getSession().setAttribute("fechaNaciento", usuario.getFechaNacimiento());

            List<GrantedAuthority> authorities = new ArrayList<>();
            usuario.getPermisos().forEach(permiso -> {
                authorities.add(new SimpleGrantedAuthority(permiso.name()));
            });

            usuario.getRoles().forEach(rol -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.name()));
            });

            return new UsernamePasswordAuthenticationToken(username, password, authorities);

        } catch (RuntimeException e) {
            throw new BadCredentialsException("Error en el sistema de autenticación: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}