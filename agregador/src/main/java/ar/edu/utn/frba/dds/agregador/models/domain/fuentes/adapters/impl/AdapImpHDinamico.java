package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class AdapImpHDinamico implements IAdapImpH {

  @Getter
  private static final AdapImpHDinamico instance = new AdapImpHDinamico();

  private AdapImpHDinamico() {}

  @Override
  public Flux<Hecho> importarHechos(WebClient webClient, Fuente fuente) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoInputDTO>>() {})
        .flatMapMany(dtos -> Flux.fromIterable(HechoInputDTO.mapDTOToHechos(dtos, fuente)))
        .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
            .maxBackoff(Duration.ofSeconds(10)))
        .onErrorResume(error -> {
          System.err.println("Error importando hechos de fuente dinámica " + fuente.getIdInternoFuente() + ": " + error.getMessage());
          return Flux.empty();
        });
  }

  public Flux<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco,
                                        WebClient webClient,
                                        Fuente fuente) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoInputDTO>>() {})
        .flatMapMany(dtos -> Flux.fromIterable(HechoInputDTO.mapDTOToHechos(dtos, fuente)))
        .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
            .maxBackoff(Duration.ofSeconds(10)))
        .onErrorResume(error -> {
          System.err.println("Error buscando nuevos hechos de fuente dinámica " + fuente.getIdInternoFuente() + ": " + error.getMessage());
          return Flux.empty();
        });
  }

  @Override
  public Mono<Void> eliminarHecho(Hecho hecho, WebClient webClient, Fuente fuente) {
    return webClient.patch()
        .uri(uriBuilder -> uriBuilder
            .path("/eliminacion/{id}")
            .build(hecho.getIdInternoFuente(fuente)))
        .bodyValue(HechoOutputDTO.HechoToDTO(hecho))
        .retrieve()
        .toBodilessEntity()
        .then()
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)))
        .onErrorResume(error -> {
          System.err.println("Error eliminando hecho de fuente dinámica " + fuente.getIdInternoFuente() + ": " + error.getMessage());
          return Mono.empty();
        });
  }

  @Override
  public Flux<Hecho> importarHechosMismoTitulo(WebClient webClient, Fuente fuente, Hecho hechos) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .queryParam("titulo", hechos.getTitulo())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoInputDTO>>() {})
        .flatMapMany(dtos -> Flux.fromIterable(HechoInputDTO.mapDTOToHechos(dtos, fuente)))
        .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
            .maxBackoff(Duration.ofSeconds(10)))
        .onErrorResume(error -> {
          System.err.println("Error importando hechos mismo título de fuente dinámica " + fuente.getIdInternoFuente() + ": " + error.getMessage());
          return Flux.empty();
        });
  }

    @Override
    public Flux<Hecho> importarNuevos(WebClient webClient, Fuente fuente) {
        return null;
    }
}
