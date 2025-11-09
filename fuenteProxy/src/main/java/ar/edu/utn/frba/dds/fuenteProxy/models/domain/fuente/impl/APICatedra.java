package ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.PaginaHechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores.RequestLogin;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.autentificadores.ResponseLogin;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Implementación de TipoFuente para la API de la cátedra.
 * <p>
 * PATRÓN SINGLETON:
 *  - Sólo debe existir una instancia porque siempre se usa la misma ruta y credenciales.
 *  - Se expone getInstance(ruta) con doble chequeo (double-checked) sobre INSTANCE.
 * <p>
 * CONCURRENCIA / THREAD-SAFETY:
 *  - INSTANCE es volatile para garantizar visibilidad de la referencia una vez creada.
 *  - synchronized(APICatedra.class) en la inicialización evita que dos hilos creen la instancia a la vez.
 *  - token es volatile para que otros hilos vean el valor actualizado inmediatamente tras el login.
 *  - ensureAuthenticated() hace un doble chequeo sincronizado sobre "this" para evitar logins duplicados si varias
 *    peticiones llegan al mismo tiempo antes de inicializar el token.
 * <p>
 * VOLATILE:
 *  - Asegura visibilidad (lecturas ven el último valor escrito) y evita reordenamientos peligrosos.
 *  - No provee exclusión mutua; por eso se combina con synchronized donde es necesario.
 * <p>
 * SINCRONIZACIÓN:
 *  - Usada sólo en las rutas críticas (creación de instancia y obtención de token) para minimizar contención.
 */
public class APICatedra implements TipoFuente {
    private WebClient webClient;
    // token se marca volatile para asegurar que los hilos que lean luego de inicializarlo vean el valor actualizado.
    private volatile String token;
    private final String ruta;
    private static final Logger logger = LoggerFactory.getLogger(APICatedra.class);

    // Singleton (referencia compartida). volatile para uso seguro con doble chequeo.
    private static volatile APICatedra INSTANCE;

    /**
     * Obtiene la instancia singleton. Si aún no existe la crea de forma thread-safe.
     * El parámetro ruta sólo se usa la primera vez; posteriores llamadas lo ignoran si la instancia ya está creada.
     */
    public static APICatedra getInstance(String ruta) {
        if (INSTANCE == null) { // Primer chequeo (rápido, sin bloqueo)
            synchronized (APICatedra.class) { // Bloqueo sólo si aún es null
                if (INSTANCE == null) { // Segundo chequeo dentro del bloqueo
                    INSTANCE = new APICatedra(ruta);
                }
            }
        }
        return INSTANCE;
    }

    // Constructor privado: fuerza uso de getInstance()
    private APICatedra(String ruta) {
        this.ruta = ruta == null || ruta.isBlank() ? "https://api-ddsi.disilab.ar/public" : ruta.trim();
        this.webClient = WebClient.builder().baseUrl(this.ruta).build();
    }

    @Override
    public Flux<InputHecho> getAll() {
        ensureAuthenticated();
        return getHechosPagina(this.ruta + "/api/desastres");
    }

    @Override
    public Mono<InputHecho> getById(Long id) {
        ensureAuthenticated();
        return webClient
                .get()
                .uri("/api/desastres/{id}", id)
                .retrieve()
                .bodyToMono(InputHecho.class);
    }

    @Override
    public Flux<InputHecho> getNuevos(LocalDateTime date) {
        // La API Cátedra no expone delta de novedades por fecha.
        return Flux.empty();
    }

    @Override
    public Flux<InputColeccionDTO> getAllColecciones() {
        return Flux.fromIterable(new ArrayList<>()); // La API cátedra no entiende de colecciones.
    }

    @Override
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol) {
        // TODO: implementar si la API lo soporta.
    }

    /**
     * Inicializa el token si aún no se autenticó la instancia. Se usa doble chequeo + synchronized para evitar
     * llamadas concurrentes a login(). Si ya hay token, no se hace bloqueo.
     */
    private void ensureAuthenticated() {
        if (this.token == null) { // Chequeo rápido sin bloqueo
            synchronized (this) { // Bloqueo sólo si parece no inicializado
                if (this.token == null) { // Segundo chequeo dentro del bloqueo
                    logger.info("[APICatedra] Autenticando contra API en ruta={}", this.ruta);
                    this.token = this.login();
                    this.webClient = WebClient.builder()
                            .baseUrl(this.ruta)
                            .defaultHeader("Authorization", "Bearer " + this.token)
                            .build();
                }
            }
        }
    }

    /**
     * Realiza el login contra la API y devuelve el access token.
     * Maneja casos de respuesta nula o token faltante con RuntimeException para escalarlos al caller.
     */
    private String login() {
        RequestLogin requestLogin = new RequestLogin("ddsi@gmail.com", "ddsi2025*");
        String correlationId = UUID.randomUUID().toString();
        long startNs = System.nanoTime();
        logger.debug("[APICatedra] Login iniciado corrId={}", correlationId);
        try {
            ResponseLogin response = this.webClient.post()
                    .uri("/api/login")
                    .header("X-Correlation-Id", correlationId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(requestLogin)
                    .exchangeToMono(cr -> manejarRespuestaLogin(cr, correlationId))
                    .block();

            long ms = (System.nanoTime() - startNs) / 1_000_000;
            if (response == null) {
                logger.error("[APICatedra] Login respuesta nula corrId={} durMs={}", correlationId, ms);
                throw new RuntimeException("Login: respuesta nula del servidor");
            }
            if (response.getData() == null || response.getData().getAccess_token() == null || response.getData().getAccess_token().isBlank()) {
                logger.error("[APICatedra] Login sin token corrId={} durMs={}", correlationId, ms);
                throw new RuntimeException("Login: token ausente en la respuesta");
            }
            logger.debug("[APICatedra] Login OK corrId={} durMs={}", correlationId, ms);
            return response.getData().getAccess_token();
        } catch (RuntimeException ex) {
            long ms = (System.nanoTime() - startNs) / 1_000_000;
            String msg = ex.getMessage();
            if (msg == null || msg.isBlank()) msg = "Error desconocido durante login";
            logger.error("[APICatedra] Login fallo corrId={} durMs={} detalle='{}'", correlationId, ms, msg);
            throw new RuntimeException("Fallo en login API Cátedra (corrId=" + correlationId + "): " + msg, ex);
        }
    }

    private Mono<ResponseLogin> manejarRespuestaLogin(ClientResponse clientResponse, String correlationId) {
        if (clientResponse.statusCode().is2xxSuccessful()) {
            return clientResponse.bodyToMono(ResponseLogin.class);
        }
        return clientResponse.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body -> {
                    logger.error("[APICatedra] Login HTTP error status={} corrId={} body='{}'", clientResponse.statusCode(), correlationId, body);
                    return Mono.error(new RuntimeException("HTTP " + clientResponse.statusCode() + " durante login (corrId=" + correlationId + "). Body: " + body));
                });
    }

    private Flux<InputHecho> getHechosPagina(String url) {
        // Por seguridad volvemos a verificar autenticación (idempotente si token ya está).
        ensureAuthenticated();
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(PaginaHechoDTO.class)
                .flatMapMany(pagina -> {
                    Flux<InputHecho> hechosActuales = Flux.fromIterable(pagina.getData());
                    if (pagina.getNext_page_url() != null) {
                        return hechosActuales.concatWith(getHechosPagina(pagina.getNext_page_url()));
                    } else {
                        return hechosActuales;
                    }
                });
    }
}
