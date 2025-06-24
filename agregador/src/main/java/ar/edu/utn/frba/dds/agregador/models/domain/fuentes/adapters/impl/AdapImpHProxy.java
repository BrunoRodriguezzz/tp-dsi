package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
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
  public List<Hecho> importarHechos(WebClient webClient) {
    try {
      List<FuenteResponseDTO> respuesta = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/hechos")
              .build())
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<List<FuenteResponseDTO>>() {})
          .doOnError(error -> {
            // Registro detallado del error
            System.err.println("Error al obtener los hechos: " + error.getMessage());
            error.printStackTrace();
          })
          .onErrorMap(error -> {
            // Transformar la excepción en una más específica
            return new RuntimeException("Error al consumir el servicio de hechos", error);
          })
          .block();

      if (respuesta == null) {
        return Collections.emptyList();
      }

      return UtilsDTO.servicioResponseToHechos(respuesta);
    } catch (Exception e) {
      // Manejo de excepciones no controladas
      System.err.println("Excepción inesperada: " + e.getMessage());
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  @Override
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco, WebClient webClient) {
    List<FuenteResponseDTO> respuesta = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<FuenteResponseDTO>>() {})
        .block();

    List<Hecho> respuestaFinal = new ArrayList<>();
    respuesta.stream().map(response -> {
      List<Hecho> hechos = UtilsDTO.servicioResponseToHechos(respuesta);
      respuestaFinal.addAll(hechos);
      return hechos;
    }).toList(); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
    return respuestaFinal;
  }

  @Override
  public void eliminarHecho(Hecho hecho, WebClient webClient) {
    webClient.delete()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos/{id}")
            .build(hecho.getIdInternoFuente()))
        .retrieve()
        .toBodilessEntity()
        .block();
  }
}
