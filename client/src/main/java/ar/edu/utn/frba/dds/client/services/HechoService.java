package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.Params;
import ar.edu.utn.frba.dds.client.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDinamicaDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoRevisadoForm;
import ar.edu.utn.frba.dds.client.dtos.hecho.PaginadoHechoDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class HechoService {
  private final WebApiCallerService webApiCallerService;

  @Value("${servicio.agregador}")
  private String agregadorURL;

  public HechoService(
      WebApiCallerService webApiCallerService) {
    this.webApiCallerService = webApiCallerService;
  }

  public List<HechoDTO> obtenerHechos() {
    try {
      return Objects.requireNonNull(WebClient.builder().baseUrl(agregadorURL)
                      .build()
                      .get()
                      .uri("/hechos")
                      .retrieve()
                      .bodyToMono(PaginadoHechoDTO.class)
                      .block())
              .getContent();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public PaginadoHechoDTO obtenerHechosAgregador() {
    try {
      return this.webApiCallerService.get(this.agregadorURL + "/hechos", PaginadoHechoDTO.class);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public PaginadoHechoDTO obtenerHechosAgregadorFiltrados(Params params) {
    try {
      PaginadoHechoDTO paginado = WebClient.builder().baseUrl(this.agregadorURL)
          .build()
          .get()
          .uri(uriBuilder -> {
            var uri = uriBuilder.path("/hechos")
                .queryParam("fechaAcontecimientoInicio", params.getFechaAcontecimientoInicio())
                .queryParam("fechaAcontecimientoFin", params.getFechaAcontecimientoFin())
                .queryParam("categoria", params.getCategoria())
                .queryParam("latitud", params.getLat())
                .queryParam("longitud", params.getLng())
                .queryParam("fuente", params.getFuente())
                .queryParam("page", params.getPage())
                .queryParam("size", params.getSize())
                .build();
            log.info("Requesting URL: {}", uri);
            return uri;
          })
          .retrieve()
          .bodyToMono(PaginadoHechoDTO.class)
          .block();
      log.info("Recibi: {}", paginado);
      return paginado;
    } catch (Exception e) {
      log.error(e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  public HechoDTO obtenerHechoPorId(Long id) {
    try {
        return WebClient.builder()
                .baseUrl(agregadorURL)
                .build()
                .get()
                .uri("/hechos/" + id)
                .retrieve()
                .bodyToMono(HechoDTO.class)
                .block();
    } catch (Exception e) {
      return null;
    }
  }

  public boolean modificarHecho(Long id, HechoDTO hechoDTO) {
    try {
      webApiCallerService.post(agregadorURL + "/hechos/" + id, hechoDTO, Void.class);
      return true;
    } catch (Exception e) {
      log.error(e.getMessage());
      return false;
    }
  }

  public boolean enviarSolicitudEliminacion(SolicitudEliminacionDTO solicitud) {
    try {
      log.info("Enviando solicitud de eliminación: {}", solicitud);
      webApiCallerService.post(agregadorURL + "/solicitudesEliminacion", solicitud, Void.class);
      log.info("Solicitud de eliminación enviada exitosamente");
      return true;
    } catch (Exception e) {
      log.error("Error al enviar solicitud de eliminación: {}", e.getMessage(), e);
      return false;
    }
  }
}