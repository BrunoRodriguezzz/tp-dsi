package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.FuenteResponseDTO;
import java.time.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class AdapImpH implements IAdapImpH {

  @Getter
  private static final AdapImpH instance = new AdapImpH();

  // Configuration Constantes
  private static final Duration TIMEOUT = Duration.ofSeconds(45);
  private static final int MAX_RETRIES = 2;
  private static final Duration INITIAL_BACKOFF = Duration.ofSeconds(2);
  private static final Duration MAX_BACKOFF = Duration.ofSeconds(15);

  private AdapImpH() {}

  @Override
  public Flux<Hecho> importarHechos(WebClient webClient, Fuente fuente) {
    return executeGetRequest(
        webClient,
        "/hechos/fuente/{id}",
        "importación de hechos",
        fuente,
        fuente.getIdInternoFuente()
    );
  }

    @Override
    public Mono<Void> eliminarHecho(Hecho hecho, WebClient webClient, Fuente fuente) {
        Long idInterno = hecho.getIdInternoFuente(fuente);
        String operationName = "eliminación de hecho";
        log.info("Eliminando hecho ID: {} de fuente ID: {}", idInterno, fuente.getIdInternoFuente());

        return webClient.delete()
                .uri("/eliminacion/{id}", idInterno)
                .retrieve()
                .toBodilessEntity()
                .then()
                .doOnError(error -> log.error("Error eliminando hecho ID: {} de fuente ID: {}. Tipo: {}, Mensaje: {}",
                        idInterno, fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage()))
                .retryWhen(Retry.backoff(1, Duration.ofSeconds(2))
                        .doBeforeRetry(retrySignal -> log.warn("Reintentando eliminarHecho ID: {} para fuente ID: {}. Intento: {}",
                                idInterno, fuente.getIdInternoFuente(), retrySignal.totalRetries() + 1)))
                .onErrorResume(error -> {
                    logError(error, operationName, String.valueOf(fuente.getIdInternoFuente()));
                    return Mono.empty();
                })
                .doOnSuccess(result -> log.info("Hecho ID: {} eliminado exitosamente de fuente ID: {}", idInterno, fuente.getIdInternoFuente()));
    }

  @Override
  public Flux<Hecho> importarHechosMismoTitulo(WebClient webClient,
                                               Fuente fuente,
                                               Hecho hecho) {
    return executeGetRequest(
        webClient,
        "/hechos/fuente/{id}?titulo={titulo}",
        "importación de hechos con mismo título",
        fuente,
        fuente.getIdInternoFuente(),
        hecho.getTitulo()
    );
  }

  @Override
  public Flux<Hecho> importarNuevos(WebClient webClient, Fuente fuente) {
    return executeGetRequest(
        webClient,
        "/hechos/fuente/{id}?nuevos=true",
        "importación de hechos nuevos",
        fuente,
        fuente.getIdInternoFuente()
    );
  }

  private Flux<Hecho> executeGetRequest(
      WebClient webClient,
      String uriTemplate,
      String operationName,
      Fuente fuente,
      Object... uriVariables) {

    log.info("Iniciando {} para fuente ID: {}", operationName, fuente.getIdInternoFuente());

    Mono<FuenteResponseDTO> responseMono = webClient.get()
        .uri(uriTemplate, uriVariables)
        .retrieve()
        .bodyToMono(FuenteResponseDTO.class);

    return responseMono
        .flatMap(response -> Mono.fromCallable(() -> FuenteResponseDTO.toHechos(response, fuente))
            .doOnSuccess(hechos -> log.info("Convertidos {} hechos para {}. Fuente ID: {}",
                hechos.size(), operationName, fuente.getIdInternoFuente()))
            .doOnError(e -> log.error("Error de conversión para {}. Fuente ID: {}. Error: {}",
                operationName, fuente.getIdInternoFuente(), e.getMessage()))
            .onErrorMap(IllegalArgumentException.class, e -> new RuntimeException("Error de conversión", e))
        )
        .flatMapMany(Flux::fromIterable)
        .doOnError(error -> log.error("Error en flujo {} para fuente ID: {}. Tipo: {}, Mensaje: {}",
            operationName, fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage()))
        .timeout(TIMEOUT)
        .retryWhen(Retry.backoff(MAX_RETRIES, INITIAL_BACKOFF)
            .maxBackoff(MAX_BACKOFF)
            .doBeforeRetry(retrySignal -> log.warn("Reintentando {} para fuente ID: {}. Intento: {}",
                operationName, fuente.getIdInternoFuente(), retrySignal.totalRetries() + 1)))
        .onErrorResume(error -> {
            logError(error, operationName, String.valueOf(fuente.getIdInternoFuente()));
            return Flux.empty();
        })
        .doOnComplete(() -> log.info("{} completada para fuente ID: {}", operationName, fuente.getIdInternoFuente()));
  }

  private void logError(Throwable error, String operation, String fuenteId) {
    if (error instanceof WebClientResponseException wcre) {
      log.error("Error HTTP {} fuente ID: {}. Status: {}, Body: {}",
          operation, fuenteId, wcre.getStatusCode(), wcre.getResponseBodyAsString());
    } else {
      log.error("Error {} fuente ID: {}. Tipo: {}, Mensaje: {}",
          operation, fuenteId, error.getClass().getSimpleName(), error.getMessage(), error);
    }
  }
}
