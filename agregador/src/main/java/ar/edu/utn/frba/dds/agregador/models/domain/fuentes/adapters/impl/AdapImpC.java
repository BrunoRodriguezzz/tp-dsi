package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapImpC;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.FuenteResponseDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class AdapImpC implements IAdapImpC {
  // Clase Singleton
  @Getter // Método público para obtener las instancia
  private static final AdapImpC instance = new AdapImpC(); // Creacion de la instancia clase única
  private AdapImpC() {} // Constructor privado para evitar instanciación externa

  // TODO: Falta probar con otra instancia MetaMapa
  public List<Coleccion> importarColecciones(WebClient webClient, Long idInternoFuente) {
//    List <Coleccion> colecciones = webClient.get()
//        .uri(uriBuilder -> uriBuilder
//            .path("/hechos") // TODO: Corregir menos harcodeado
//            .build())
//        .retrieve()
//        .bodyToMono(new ParameterizedTypeReference<List<ColeccionInputDTO>>() {})
//        .map(c -> ColeccionInputDTO.inputColeccionToColeccion(c,null)).block();
//    return colecciones;
//    return new ArrayList<Coleccion>();
    return new ArrayList<>();
  }

  // TODO: Falta probar con otra instancia MetaMapa
  public Coleccion importarColeccion(WebClient webClient, Long id, Long idInternoFuente) {
    Coleccion coleccion = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/coleccion/{id}") // TODO: Corregir menos harcodeado
            .build(id))
        .retrieve()
        .bodyToMono(ColeccionInputDTO.class)
        .map(coleccionInputDTO -> ColeccionInputDTO.inputColeccionToColeccion(coleccionInputDTO, null)) // TODO: Crear nuevo InputColeccion para poder recibir las colecciones con hechos de la proxy.
        .block();
    return coleccion;
  }

  // TODO: Falta probar con otra instancia MetaMapa
  public List<Hecho> importarHechosColeccion(WebClient webClient, Long id, Long idInternoFuente) {
    List<FuenteResponseDTO> respuesta = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos/filtered")
            .queryParam("fuenteId", idInternoFuente)
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
}
