package ar.edu.utn.frba.dds.agregador.services.impl.adapters;

import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.FuenteResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.HechoServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.ServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.services.IFuenteAdapter;
import ar.edu.utn.frba.dds.agregador.services.impl.TipoFuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class FuenteAdapter implements IFuenteAdapter {
  private final WebClient fuenteAPI;

  private final TipoFuente tipoFuente;

  public FuenteAdapter(String baseUrl, TipoFuente tipoFuente) {
    this.fuenteAPI = WebClient.builder()
        .baseUrl(baseUrl)
        .build();
    this.tipoFuente = tipoFuente;
  }

  public TipoFuente getTipoFuente() {
    return tipoFuente;
  }

  public List<Hecho> buscarHechos() {
    try {
      List<FuenteResponseDTO> respuesta = fuenteAPI.get()
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

      return servicioResponseToHechos(respuesta);
    } catch (Exception e) {
      // Manejo de excepciones no controladas
      System.err.println("Excepción inesperada: " + e.getMessage());
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

//  public List<Hecho> buscarHechos() {
//    List<FuenteResponseDTO> respuesta = fuenteAPI.get()
//        .uri(uriBuilder -> uriBuilder
//            .path("/hechos")
//            .build())
//        .retrieve()
//        .bodyToMono(new ParameterizedTypeReference<List<FuenteResponseDTO>>() {})
//        .block();
//
//    List<Hecho> respuestaFinal = new ArrayList<>();
//    respuesta.stream().map(response -> {
//      List<Hecho> hechos = this.servicioResponseToHechos(respuesta);
//      respuestaFinal.addAll(hechos);
//      return hechos;
//    }).collect(Collectors.toList()); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
//    return respuestaFinal;
//  }

  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    List<FuenteResponseDTO> respuesta = fuenteAPI.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<FuenteResponseDTO>>() {})
        .block();

    List<Hecho> respuestaFinal = new ArrayList<>();
        respuesta.stream().map(response -> {
          List<Hecho> hechos = this.servicioResponseToHechos(respuesta);
          respuestaFinal.addAll(hechos);
          return hechos;
        }).collect(Collectors.toList()); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
    return respuestaFinal;
  }

  public void eliminarHecho(Hecho hecho) {
    fuenteAPI.delete()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos/{id}")
            .build(hecho.getId()))
        .retrieve()
        .toBodilessEntity()
        .block();
  }

  //Privados
  public List<Hecho> servicioResponseToHechos(List<FuenteResponseDTO> servicioResponse) {
    List<Hecho> hechos = new ArrayList<>();
    servicioResponse.forEach(fuente -> {
      List<Hecho> hechosEnFuente = fuente
          .getHechos()
          .stream().map(h -> {
            Hecho hecho = UtilsDTO.hechoServicioResponseDTOtoHecho(h);
            hecho.setFuente(fuente.getNombre());
            return hecho;
          })
          .collect(Collectors.toList());
      hechos.addAll(hechosEnFuente);
    });
    return hechos;
  }
}
