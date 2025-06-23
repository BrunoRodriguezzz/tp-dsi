package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.HechoServicioResponseDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class AdapImpHDinamico implements IAdapImpH {
  // Clase Singleton
  @Getter // Método público para obtener las instancia
  private static final AdapImpHDinamico instance = new AdapImpHDinamico(); // Creacion de la instancia clase única
  private AdapImpHDinamico() {} // Constructor privado para evitar instanciación externa

  @Override
  public List<Hecho> importarHechos(WebClient webClient) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos") // TODO: Corregir menos harcodeado
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoServicioResponseDTO>>() {})
        .map(this::servicioResponseToHechos).block();
  }

  @Override
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco, WebClient webClient) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoServicioResponseDTO>>() {})
        .map(this::servicioResponseToHechos).block(); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
  }

  @Override
  public void eliminarHecho(Hecho hecho, WebClient webClient) {
    webClient.patch()
        .uri(uriBuilder -> uriBuilder
            .path("/eliminacion/{id}")
            .build(hecho.getIdHFuente()))
        .bodyValue(UtilsDTO.HechoToDTO(hecho))
        .retrieve()
        .toBodilessEntity()
        .block();
  }

  private List<Hecho> servicioResponseToHechos(List<HechoServicioResponseDTO> hechosDTO) {
    return hechosDTO
        .stream()
        .map(UtilsDTO::hechoServicioResponseDTOtoHecho).collect(Collectors.toList());
  }
}
