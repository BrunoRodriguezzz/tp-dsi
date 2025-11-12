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

@Slf4j
@Service
public class HechoService {
  private final WebClient webClient;
  private final WebApiCallerService webApiCallerService;
  private final String hechoServiceUrl;
  private final MockService mockService;
  private final String agregadorURL = "http://localhost:8082";

  public HechoService(
      WebApiCallerService webApiCallerService,
      @Value("${hechos.service.url}") String hechoServiceUrl) {
    this.webClient = WebClient.builder().build();
    this.webApiCallerService = webApiCallerService;
    this.hechoServiceUrl = hechoServiceUrl;
    this.mockService = new MockService();
  }

  public List<HechoDTO> obtenerHechosDestacados() {
//        List<HechoDTO> response = this.webApiCallerService.getList(this.hechoServiceUrl, HechoDTO.class);
//        return response != null ? response : List.of();
    return this.mockService.obtenerHechosMockeados();
  }

  public List<HechoDTO> obtenerHechos() {
    return mockService.obtenerHechosMockeados();
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
      return this.webApiCallerService.get(this.agregadorURL + "/hechos/" + id, HechoDTO.class);
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