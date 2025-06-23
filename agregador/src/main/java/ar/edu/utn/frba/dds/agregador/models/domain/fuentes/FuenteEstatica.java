package ar.edu.utn.frba.dds.agregador.models.domain.fuentes;

import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.FuenteResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class FuenteEstatica extends Fuente {
  private WebClient fuenteAPI;

  public FuenteEstatica(String baseUrl) {
    this.fuenteAPI = WebClient.builder()
        .baseUrl(baseUrl)
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
        .build();
  }

  public List<Hecho> importarHechos() {
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

  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    return new ArrayList<>();
  }

  public void eliminarHecho(Hecho hecho) {

  }

  //Privados
  private List<Hecho> servicioResponseToHechos(List<FuenteResponseDTO> servicioResponse) {
    List<Hecho> hechos = new ArrayList<>();
    servicioResponse.forEach(fuente -> {
      List<Hecho> hechosEnFuente = fuente
          .getHechos()
          .stream().map(h -> {
            Hecho hecho = UtilsDTO.hechoServicioResponseDTOtoHecho(h);
            hecho.setFuente(fuente.getNombre());
            hecho.setIdFuente(fuente.getId());
            return hecho;
          })
          .collect(Collectors.toList());
      hechos.addAll(hechosEnFuente);
    });
    return hechos;
  }
}
