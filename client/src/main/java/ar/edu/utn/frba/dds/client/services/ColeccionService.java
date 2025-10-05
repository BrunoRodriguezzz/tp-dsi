package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ColeccionService {
  private final WebClient webClient;
  private final WebApiCallerService webApiCallerService;
  private final String hechoServiceUrl;
  private final MockService mockService;

  public ColeccionService(
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

  public HechoDTO obtenerHechoPorId(Long id) {
    HechoDTO hecho = mockService.obtenerHechoPorId(id);
    return hecho;
  }
}
