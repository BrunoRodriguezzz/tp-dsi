package ar.edu.utn.frba.dds.agregador.services.impl.adapters;

import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.ServicioResponseDTO;
import ar.edu.utn.frba.dds.agregador.services.IFuenteAdapter;
import ar.edu.utn.frba.dds.agregador.services.impl.TipoFuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

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
    return fuenteAPI.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .build())
        .retrieve()
        .bodyToMono(ServicioResponseDTO.class)
        .map(response -> {
          List<Hecho> hechos = this.servicioResponseToHechos(response);
          return hechos;
        }).block(); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>

//    Mockeo trucho
//    Mono<String> respuesta = fuenteAPI.get()
//        .uri(uriBuilder -> uriBuilder
//            .path("/hechos")
//            .build())
//        .retrieve()
//        .bodyToMono(String.class);
//    String json = respuesta.block();
//    System.out.println(json);
  }

  public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco) {
    return fuenteAPI.get()
        .uri(uriBuilder -> uriBuilder
            .path("/current.json")
            .queryParam("dateTimeGT", ultimaFechaRefresco.toString())
            .build())
        .retrieve()
        .bodyToMono(ServicioResponseDTO.class)
        .map(response -> {
          List<Hecho> hechos = this.servicioResponseToHechos(response);
          return hechos;
        }).block(); // .block() me hace el codigo sincrónico para que no devuelva Mono<List<Hecho>> y devuelva List<Hecho>
  }

  public void eliminarHecho(Hecho hecho) {
    fuenteAPI.put()
        .uri(uriBuilder -> uriBuilder
            .path("/eliminacion/{id}")
            .build(hecho.getId()))
        .bodyValue(hecho)
        .retrieve()
        .toBodilessEntity() // No se espera con contenido
        .block(); // Ejecución sincrónica
  }

  //Privados
  public List<Hecho> servicioResponseToHechos(ServicioResponseDTO servicioResponse) {
    List<Hecho> hechos = new ArrayList<>();
    servicioResponse.getFuentes().forEach(fuente -> {
      List<Hecho> hechosEnFuente = fuente
          .getHechos()
          .stream().map(h -> {
            Hecho hecho = UtilsDTO.hechoServicioResponseDTOtoHecho(h);
            return hecho;
          })
          .collect(Collectors.toList());
      hechos.addAll(hechosEnFuente);
    });
    return hechos;
  }
}
