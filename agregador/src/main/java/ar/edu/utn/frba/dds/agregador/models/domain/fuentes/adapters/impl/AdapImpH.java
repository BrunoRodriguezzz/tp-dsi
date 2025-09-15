package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.FuenteResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class AdapImpH implements IAdapImpH {

  @Getter
  private static final AdapImpH instance = new AdapImpH();

  private AdapImpH() {}

  @Override
  public Flux<Hecho> importarHechos(WebClient webClient, Fuente fuente) {
    log.info("Iniciando importación de hechos para fuente ID: {}", fuente.getIdInternoFuente());

    return webClient.get()
        .uri(uriBuilder -> {
          String uri = uriBuilder.path("/hechos/filtered")
              .queryParam("fuenteId", fuente.getIdInternoFuente())
              .build().toString();
          log.info("URI construida para importarHechos: {}", uri);
          return uriBuilder.path("").build();
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<FuenteResponseDTO>>() {})
        .doOnNext(dtos -> log.info("Recibidos {} DTOs de fuente ID: {}", dtos.size(), fuente.getIdInternoFuente()))
        .flatMapMany(dtos -> {
          try {
            List<Hecho> hechos = FuenteResponseDTO.servicioResponseToHechos(dtos, fuente);
            log.info("Convertidos {} hechos exitosamente para fuente ID: {}", hechos.size(), fuente.getIdInternoFuente());
            return Flux.fromIterable(hechos);
          } catch (Exception e) {
            log.error("Error convirtiendo DTOs a Hechos para fuente ID: {}. Error: {}", fuente.getIdInternoFuente(), e.getMessage(), e);
            return Flux.error(e);
          }
        })
        .doOnError(error -> log.error("Error en flujo importarHechos para fuente ID: {}. Tipo: {}, Mensaje: {}",
            fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage()))
        .timeout(Duration.ofSeconds(45))
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))
            .maxBackoff(Duration.ofSeconds(15))
            .doBeforeRetry(retrySignal -> log.warn("Reintentando importarHechos para fuente ID: {}. Intento: {}",
                fuente.getIdInternoFuente(), retrySignal.totalRetries() + 1)))
        .onErrorResume(error -> {
          if (error instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) error;
            log.error("Error HTTP importando hechos fuente ID: {}. Status: {}, Body: {}",
                fuente.getIdInternoFuente(), wcre.getStatusCode(), wcre.getResponseBodyAsString());
          } else {
            log.error("Error importando hechos fuente ID: {}. Tipo: {}, Mensaje: {}",
                fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage(), error);
          }
          return Flux.empty();
        })
        .doOnComplete(() -> log.info("Importación de hechos completada para fuente ID: {}", fuente.getIdInternoFuente()));
  }

  @Override
  public Flux<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco,
                                        WebClient webClient,
                                        Fuente fuente) {
    log.info("Buscando nuevos hechos para fuente ID: {} desde fecha: {}", fuente.getIdInternoFuente(), ultimaFechaRefresco);

    return webClient.get()
        .uri(uriBuilder -> {
          String uri = uriBuilder
              .path("/hechos/filtered")
              .queryParam("fuenteId", fuente.getIdInternoFuente())
              .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
              .build().toString();
          log.debug("URI construida para buscarNuevosHechos: {}", uri);
          return uriBuilder
              .path("")
              .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
              .queryParam("fuenteId", fuente.getIdInternoFuente())
              .build();
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<FuenteResponseDTO>>() {})
        .doOnNext(dtos -> log.debug("Recibidos {} DTOs nuevos para fuente ID: {}", dtos.size(), fuente.getIdInternoFuente()))
        .flatMapMany(dtos -> {
          try {
            List<Hecho> hechos = FuenteResponseDTO.servicioResponseToHechos(dtos, fuente);
            log.info("Convertidos {} nuevos hechos para fuente ID: {}", hechos.size(), fuente.getIdInternoFuente());
            return Flux.fromIterable(hechos);
          } catch (Exception e) {
            log.error("Error convirtiendo DTOs a Hechos nuevos para fuente ID: {}. Error: {}", fuente.getIdInternoFuente(), e.getMessage(), e);
            return Flux.error(e);
          }
        })
        .doOnError(error -> log.error("Error en flujo buscarNuevosHechos para fuente ID: {}. Tipo: {}, Mensaje: {}",
            fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage()))
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))
            .maxBackoff(Duration.ofSeconds(15))
            .doBeforeRetry(retrySignal -> log.warn("Reintentando buscarNuevosHechos para fuente ID: {}. Intento: {}",
                fuente.getIdInternoFuente(), retrySignal.totalRetries() + 1)))
        .onErrorResume(error -> {
          if (error instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) error;
            log.error("Error HTTP buscando nuevos hechos fuente ID: {}. Status: {}, Body: {}",
                fuente.getIdInternoFuente(), wcre.getStatusCode(), wcre.getResponseBodyAsString());
          } else {
            log.error("Error buscando nuevos hechos fuente ID: {}. Tipo: {}, Mensaje: {}",
                fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage(), error);
          }
          return Flux.empty();
        })
        .doOnComplete(() -> log.info("Búsqueda de nuevos hechos completada para fuente ID: {}", fuente.getIdInternoFuente()));
  }

  @Override
  public Mono<Void> eliminarHecho(Hecho hecho, WebClient webClient, Fuente fuente) {
    log.info("Eliminando hecho ID: {} de fuente ID: {}", hecho.getIdInternoFuente(fuente), fuente.getIdInternoFuente());

    return webClient.patch()
        .uri(uriBuilder -> {
          String uri = uriBuilder
              .path("/eliminacion/{id}")
              .build(hecho.getIdInternoFuente(fuente)).toString();
          log.debug("URI construida para eliminarHecho: {}", uri);
          return uriBuilder
              .path("/eliminacion/{id}")
              .build(hecho.getIdInternoFuente(fuente));
        })
        .bodyValue(HechoOutputDTO.HechoToDTO(hecho))
        .retrieve()
        .toBodilessEntity()
        .then()
        .doOnError(error -> log.error("Error eliminando hecho ID: {} de fuente ID: {}. Tipo: {}, Mensaje: {}",
            hecho.getIdInternoFuente(fuente), fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage()))
        .retryWhen(Retry.backoff(1, Duration.ofSeconds(2))
            .doBeforeRetry(retrySignal -> log.warn("Reintentando eliminarHecho ID: {} para fuente ID: {}. Intento: {}",
                hecho.getIdInternoFuente(fuente), fuente.getIdInternoFuente(), retrySignal.totalRetries() + 1)))
        .onErrorResume(error -> {
          if (error instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) error;
            log.error("Error HTTP eliminando hecho ID: {} fuente ID: {}. Status: {}, Body: {}",
                hecho.getIdInternoFuente(fuente), fuente.getIdInternoFuente(), wcre.getStatusCode(), wcre.getResponseBodyAsString());
          } else {
            log.error("Error eliminando hecho ID: {} fuente ID: {}. Tipo: {}, Mensaje: {}",
                hecho.getIdInternoFuente(fuente), fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage(), error);
          }
          return Mono.empty();
        })
        .doOnSuccess(result -> log.info("Hecho ID: {} eliminado exitosamente de fuente ID: {}", hecho.getIdInternoFuente(fuente), fuente.getIdInternoFuente()));
  }

  @Override
  public Flux<Hecho> importarHechosMismoTitulo(WebClient webClient,
                                               Fuente fuente,
                                               Hecho hechos) {
    log.info("Importando hechos con mismo título '{}' para fuente ID: {}", hechos.getTitulo(), fuente.getIdInternoFuente());

    return webClient.get()
        .uri(uriBuilder -> {
          String uri = uriBuilder
              .path("/hechos/filtered")
              .queryParam("titulo", hechos.getTitulo())
              .build().toString();
          log.debug("URI construida para importarHechosMismoTitulo: {}", uri);
          return uriBuilder
              .path("")
              .queryParam("titulo", hechos.getTitulo())
              .build();
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoInputDTO>>() {})
        .doOnNext(dtos -> log.debug("Recibidos {} DTOs con título '{}' para fuente ID: {}", dtos.size(), hechos.getTitulo(), fuente.getIdInternoFuente()))
        .flatMapMany(dtos -> {
          try {
            List<Hecho> hechosResultado = HechoInputDTO.mapDTOToHechos(dtos, fuente);
            log.info("Convertidos {} hechos con título '{}' para fuente ID: {}", hechosResultado.size(), hechos.getTitulo(), fuente.getIdInternoFuente());
            return Flux.fromIterable(hechosResultado);
          } catch (Exception e) {
            log.error("Error convirtiendo DTOs a Hechos para título '{}' fuente ID: {}. Error: {}", hechos.getTitulo(), fuente.getIdInternoFuente(), e.getMessage(), e);
            return Flux.error(e);
          }
        })
        .doOnError(error -> log.error("Error en flujo importarHechosMismoTitulo título '{}' fuente ID: {}. Tipo: {}, Mensaje: {}",
            hechos.getTitulo(), fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage()))
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))
            .maxBackoff(Duration.ofSeconds(15))
            .doBeforeRetry(retrySignal -> log.warn("Reintentando importarHechosMismoTitulo título '{}' fuente ID: {}. Intento: {}",
                hechos.getTitulo(), fuente.getIdInternoFuente(), retrySignal.totalRetries() + 1)))
        .onErrorResume(error -> {
          if (error instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) error;
            log.error("Error HTTP importando hechos título '{}' fuente ID: {}. Status: {}, Body: {}",
                hechos.getTitulo(), fuente.getIdInternoFuente(), wcre.getStatusCode(), wcre.getResponseBodyAsString());
          } else {
            log.error("Error importando hechos título '{}' fuente ID: {}. Tipo: {}, Mensaje: {}",
                hechos.getTitulo(), fuente.getIdInternoFuente(), error.getClass().getSimpleName(), error.getMessage(), error);
          }
          return Flux.empty();
        })
        .doOnComplete(() -> log.info("Importación de hechos con título '{}' completada para fuente ID: {}", hechos.getTitulo(), fuente.getIdInternoFuente()));
  }
}