package ar.edu.utn.frba.dds.agregador.models.domain;

import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.HechoServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.services.impl.FuenteService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class FuenteDinamica extends Fuente {
  private WebClient fuenteAPI;

  public FuenteDinamica(String baseUrl) {
    this.fuenteAPI = WebClient.builder()
        .baseUrl(baseUrl + "/api/fuenteDinamica")
        .build();
  }

  public List<Hecho> importarHechos() {
    return fuenteAPI.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos") // TODO: Corregir menos harcodeado
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoServicioResponseDTO>>() {})
        .map(response -> {
          List<Hecho> hechos = this.servicioResponseToHechos(response);
          return hechos;
        }).block();
  }

  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    return fuenteAPI.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoServicioResponseDTO>>() {})
        .map(response -> {
          List<Hecho> hechos = this.servicioResponseToHechos(response);
          return hechos;
        }).block(); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
  }

  public void eliminarHecho(Hecho hecho) {
    fuenteAPI.patch()
        .uri(uriBuilder -> uriBuilder
            .path("/eliminacion/{id}")
            .build(hecho.getIdHFuente()))
        .bodyValue(UtilsDTO.HechoToDTO(hecho))
        .retrieve()
        .toBodilessEntity()
        .block();
  }

  public List<Hecho> servicioResponseToHechos(List<HechoServicioResponseDTO> hechosDTO) {
    List<Hecho> hechos = hechosDTO
        .stream()
        .map(h -> {
          Hecho hecho = UtilsDTO.hechoServicioResponseDTOtoHecho(h);
          return hecho;
        }).collect(Collectors.toList());
    return hechos;
  }
}
