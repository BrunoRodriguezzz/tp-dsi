package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.client.dtos.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.client.dtos.PaginaDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.PaginadoHechoDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ColeccionService {
  private final WebClient webClient;
  private final WebApiCallerService webApiCallerService;
  private final String hechoServiceUrl;
  private final MockService mockService;

  @Autowired
  private HechoService hechoService;

  public ColeccionService(
      WebApiCallerService webApiCallerService,
      @Value("${servicio.apiGateway}") String hechoServiceUrl) {
    this.webClient = WebClient.builder().build();
    this.webApiCallerService = webApiCallerService;
    this.hechoServiceUrl = hechoServiceUrl;
    this.mockService = new MockService();
  }

  public List<HechoDTO> obtenerHechosDestacados() {
//        List<HechoDTO> response = this.webApiCallerService.getList(this.hechoServiceUrl + "/hechos", HechoDTO.class);
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

  public List<ColeccionOutputDTO> obtenerColecciones() {
    WebClient client = WebClient.builder().baseUrl(this.hechoServiceUrl).build();
    PaginaDTO<ColeccionOutputDTO> pagina = client.get()
        .uri("/colecciones?all=true")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PaginaDTO<ColeccionOutputDTO>>() {})
        .block();
    return pagina != null && pagina.getContent() != null ? pagina.getContent() : List.of();
  }

  public ColeccionOutputDTO obtenerColeccionPorId(Long id) {
    return this.obtenerColecciones().stream()
        .filter(c -> c.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  // TODO: esto llama a todos los hechos del agrgador, no a los de una coleccion en particular
  public List<HechoDTO> obtenerHechosPorColeccionId(Long coleccionId) {
    PaginadoHechoDTO paginado = hechoService.obtenerHechosAgregador();
    if (paginado != null && paginado.getContent() != null) {
      return paginado.getContent();
    }
    return List.of();
  }

  public void enviarColeccion(ColeccionInputDTO coleccionInputDTO) {
      webApiCallerService.post(this.hechoServiceUrl + "/colecciones",coleccionInputDTO, Void.class);
  }

  public void modificarColeccion(ColeccionInputDTO coleccionInputDTO, Long id){
      webApiCallerService.put(this.hechoServiceUrl +  "/colecciones/" + id,coleccionInputDTO, Void.class);
  }

  public void actualizarFuente(String nombreFuente) {
      webApiCallerService.post("/fuentes/{nombreFuente}/actualizar-colecciones",nombreFuente, String.class);
  }
}
