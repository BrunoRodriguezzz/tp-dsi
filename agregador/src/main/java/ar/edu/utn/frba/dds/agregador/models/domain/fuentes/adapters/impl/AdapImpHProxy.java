package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.FuenteResponseDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class AdapImpHProxy implements IAdapImpH {
  // Clase Singleton
  @Getter // Método público para obtener las instancia
  private static final AdapImpHProxy instance = new AdapImpHProxy(); // Creacion de la instancia clase única
  private AdapImpHProxy() {} // Constructor privado para evitar instanciación externa

  @Override
  public List<Hecho> importarHechos(WebClient webClient, Long idInternoFuente) {
    try {
      List<FuenteResponseDTO> respuesta = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/hechos/filtered")
              .queryParam("fuenteId", idInternoFuente)
              .build())
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<List<FuenteResponseDTO>>() {})
          .onErrorMap(error -> {
            System.err.println("ERROR al hacer request: " + error.getClass().getSimpleName() + " - " + error.getMessage());
            error.printStackTrace(); // esto te da todo el detalle
            return new RuntimeException("Error al consumir el servicio de hechos", error);
          })
          .block();

      if (respuesta == null) {
        return Collections.emptyList();
      }

      return FuenteResponseDTO.servicioResponseToHechos(respuesta);
    } catch (Exception e) {
      System.err.println("Excepción inesperada: " + e.getMessage());
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  @Override
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco, WebClient webClient, Long idInternoFuente) {
    List<FuenteResponseDTO> respuesta = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos/filtered")
            .queryParam("fuenteId", idInternoFuente)
            .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<FuenteResponseDTO>>() {})
        .block();
    List<Hecho> respuestaFinal = new ArrayList<>();
    respuesta.stream().map(response -> {
      List<Hecho> hechos = FuenteResponseDTO.servicioResponseToHechos(respuesta);
      respuestaFinal.addAll(hechos);
      return hechos;
    }).toList(); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
    return respuestaFinal;
  }

  @Override
  public void eliminarHecho(Hecho hecho, WebClient webClient, Long idInternoFuente) {
    webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos/{id}")
            .build(hecho.getIdInternoFuente()))
        .retrieve()
        .toBodilessEntity()
        .block();
  }
}
