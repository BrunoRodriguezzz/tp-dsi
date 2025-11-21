package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.client.dtos.auth.UsuarioDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class AuthApiService {
    private static final Logger log = LoggerFactory.getLogger(AuthApiService.class);
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String authServiceUrl;
    @Autowired
    public AuthApiService(
            WebApiCallerService webApiCallerService,
            @Value("${servicio.auth}") String authServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.authServiceUrl = authServiceUrl;
    }

    public AuthResponseDTO login(String username, String password) {
        try {
            AuthResponseDTO response = webClient
                    .post()
                    .uri(authServiceUrl)
                    .bodyValue(Map.of(
                            "username", username,
                            "password", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode errorBody = mapper.readTree(e.getResponseBodyAsString());

                String message = errorBody.has("message") ? errorBody.get("message").asText() : "Error desconocido";

                log.warn("Mensaje de error recibido: {}", message);

                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new RuntimeException("Login fallido: " + message);
                }

                // Otros errores HTTP
                throw new RuntimeException("Error en autenticación: " + message);

            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Error procesando respuesta de error del servidor.", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Hubo un error de conexión con el servicio de autenticación.", e);
        }
    }

    public UsuarioDTO getUsuario(String accessToken) {
        try {
            UsuarioDTO response = webApiCallerService.getWithAuth(
                    authServiceUrl + "/user",
                    accessToken,
                    UsuarioDTO.class
            );
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error al obtener usuario: " + e.getMessage(), e);
        }
    }

    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> response = webApiCallerService.getList(authServiceUrl + "/users", UsuarioDTO.class);
        return response != null ? response : List.of();
    }

    public UsuarioDTO crearUsuario(@Valid UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO response = webApiCallerService.postWithoutToken(authServiceUrl + "/register", usuarioDTO, UsuarioDTO.class);
            if (response == null) {
                throw new RuntimeException("Error al registrar al usuario en el servicio externo");
            }
            return response;
        } catch (WebClientResponseException e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode errorBody = mapper.readTree(e.getResponseBodyAsString());

                String message = errorBody.has("message") ? errorBody.get("message").asText() : "Error desconocido";

                if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    throw new RuntimeException(message);
                }
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Error procesando respuesta de error del servidor.", ex);
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    public UsuarioDTO actualizarUsuario(Long id, @Valid UsuarioDTO usuarioDTO) {
        UsuarioDTO response = webApiCallerService.put(authServiceUrl + "/user/" + id, usuarioDTO, UsuarioDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al actualizar usuario en el servicio externo");
        }
        return response;
    }
}
