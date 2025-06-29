package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpH;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
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
  public List<Hecho> importarHechos(WebClient webClient, Long idInternoFuente) {
    List<Hecho> hechos = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos") // TODO: Corregir menos harcodeado
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoInputDTO>>() {})
        .map(HechoInputDTO::mapDTOToHechos).block();
    return hechos;
  }

  @Override
  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco, WebClient webClient, Long idInternoFuente) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<HechoInputDTO>>() {})
        .map(HechoInputDTO::mapDTOToHechos).block(); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
  }

  @Override
  public void eliminarHecho(Hecho hecho, WebClient webClient, Long idInternoFuente) {
    webClient.patch()
        .uri(uriBuilder -> uriBuilder
            .path("/eliminacion/{id}")
            .build(hecho.getIdInternoFuente()))
        .bodyValue(HechoOutputDTO.HechoToDTO(hecho))
        .retrieve()
        .toBodilessEntity()
        .block();
  }

  @Override
  public List<Hecho> importarHechosMismoTitulo(WebClient webClient, Long idInternoFuente, Hecho hechos) {
    // TODO
    return List.of();
  }

  private List<Hecho> servicioResponseToHechos(List<HechoInputDTO> hechosDTO, Long idInternoFuente) {
    return hechosDTO
        .stream()
        .map(HechoInputDTO::DTOToHecho).collect(Collectors.toList());
  }
}
