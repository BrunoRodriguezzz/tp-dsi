package ar.edu.utn.frba.dds.agregador.services.impl.adapters;

import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.HechoServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.ServicioDinamicoResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.ServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.services.IFuenteAdapter;
import ar.edu.utn.frba.dds.agregador.services.impl.TipoFuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class FuenteDinamicaAdapter implements IFuenteAdapter {
  private final WebClient fuenteAPI;

  private final TipoFuente tipoFuente;

  public FuenteDinamicaAdapter(String baseUrl, TipoFuente tipoFuente) {
    this.fuenteAPI = WebClient.builder()
        .baseUrl(baseUrl)
        .build();
    this.tipoFuente = tipoFuente;
  }

  public TipoFuente getTipoFuente() {
    return tipoFuente;
  }

  public List<Hecho> buscarHechos() {
    return fuenteAPI.get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/fuenteDinamica"+"/hechos") // TODO: Corregir menos harcodeado
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoServicioResponseDTO>>() {})
        .map(response -> {
          List<Hecho> hechos = this.servicioResponseToHechos(response);
          return hechos;
        }).block(); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
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
    fuenteAPI.put()
        .uri(uriBuilder -> uriBuilder
            .path("/eliminacion")
            .build(hecho.getId()))
        .bodyValue(UtilsDTO.HechoToDTO(hecho))
        .retrieve()
        .toBodilessEntity() // No se espera con contenido
        .block(); // Ejecución sincrónica
  }

  //Privados
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
