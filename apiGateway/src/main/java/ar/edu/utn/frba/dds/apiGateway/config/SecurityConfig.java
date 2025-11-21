package ar.edu.utn.frba.dds.apiGateway.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Permitir OPTIONS para CORS preflight
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        // Endpoints públicos - no requieren token
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/auth/**").permitAll()
                        // Media y recursos públicos (GET solamente)
                        .pathMatchers(HttpMethod.GET, "/media/**").permitAll()
                        // Colecciones públicas (GET), crear/editar requiere autenticación
                        .pathMatchers(HttpMethod.GET, "/colecciones/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/colecciones/**").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/colecciones/**").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/colecciones/**").authenticated()
                        // Hechos públicos (GET), editar requiere autenticación
                        .pathMatchers(HttpMethod.GET, "/hechos/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/hechos/**").permitAll()
                        .pathMatchers(HttpMethod.PUT, "/hechos/**").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/hechos/**").authenticated()
                        // Solicitudes de eliminación
                        .pathMatchers(HttpMethod.POST, "/solicitudesEliminacion/**").permitAll() // Cualquiera puede solicitar eliminar
                        .pathMatchers(HttpMethod.GET, "/solicitudesEliminacion/**").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/solicitudesEliminacion/**").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/solicitudesEliminacion/**").authenticated()
                        // Dinamica
                        .pathMatchers("/api/fuenteDinamica/gestion").authenticated()
                        .pathMatchers(HttpMethod.PUT,"/api/fuenteDinamica/modificacion").authenticated()
                        .pathMatchers(HttpMethod.GET,"/api/fuenteDinamica/modificacion").authenticated()
                        .pathMatchers(HttpMethod.GET,"/api/fuenteDinamica/pendientes/**").authenticated()
                        .pathMatchers(HttpMethod.PATCH,"/api/fuenteDinamica/eliminacion/**").authenticated()
                        .pathMatchers("/api/fuenteDinamica/hechos/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/fuenteDinamica/solicitud").permitAll()
                        .pathMatchers(HttpMethod.PATCH, "/api/fuenteDinamica/modificacion").permitAll()
                        // Estadísticas requiere autenticación
                        .pathMatchers("/estadisticas/**").authenticated()
                        // Por defecto, todo lo demás requiere autenticación
                        .anyExchange().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitir localhost en diferentes puertos para desarrollo
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        // Exponer headers importantes para el cliente
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}