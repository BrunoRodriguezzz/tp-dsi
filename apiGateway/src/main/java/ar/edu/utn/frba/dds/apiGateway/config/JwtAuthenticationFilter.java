package ar.edu.utn.frba.dds.apiGateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtValidator jwtValidator;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange.getRequest());

        if (token == null) {
            return chain.filter(exchange);
        }

        try {
            Claims claims = jwtValidator.validateToken(token);

            String username = claims.getSubject();

            String userIdString = claims.get("id").toString();

            List<String> rolesRaw = claims.get("roles", List.class);

            List<SimpleGrantedAuthority> authorities = null;
            if (rolesRaw != null) {
                authorities = rolesRaw.stream()
                        .map(rolStr -> "ROLE_" + rolStr) // Agregamos el prefijo estándar
                        .map(SimpleGrantedAuthority::new) // Convertimos a objeto de seguridad
                        .collect(Collectors.toList());
            } else {
                authorities = List.of(); // Lista vacía para evitar null pointers
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userIdString)
                    .header("X-User-Username", username)
                    // Opcional: Pasar roles en header por si el microservicio los necesita "fácil"
                    .header("X-User-Roles", String.join(",", rolesRaw != null ? rolesRaw : List.of()))
                    .build();


            return chain.filter(exchange.mutate().request(modifiedRequest).build())
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (Exception e) {
            log.warn("Token inválido o mal formado en request a {}: {}", exchange.getRequest().getPath(), e.getMessage());
            return chain.filter(exchange);
        }
    }

    private String extractToken(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String header = authHeaders.get(0);
            if (header.startsWith("Bearer ")) {
                return header.substring(7);
            }
        }
        return null;
    }
}

