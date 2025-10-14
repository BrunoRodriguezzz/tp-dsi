package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.client.dtos.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.client.dtos.FuenteOutputDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.services.ColeccionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class ColeccionController {
  private final ColeccionService coleccionService;
  private final WebClient webClient;
  private final String agregadorUrl;

  public ColeccionController(ColeccionService coleccionService,
                             @Value("${agregador.service.url:http://localhost:8082}") String agregadorUrl) {
    this.coleccionService = coleccionService;
    this.agregadorUrl = agregadorUrl;
    this.webClient = WebClient.builder().baseUrl(agregadorUrl).build();
  }

  @GetMapping("/colecciones")
  public String listarColecciones(Model model) {
    List<ColeccionOutputDTO> colecciones = coleccionService.obtenerColecciones();
    model.addAttribute("colecciones", colecciones);
    return "colecciones";
  }

  @GetMapping("/coleccion/{id}")
  public String verDetalleColeccion(@PathVariable Long id, Model model) {
    ColeccionOutputDTO coleccion = coleccionService.obtenerColeccionPorId(id);
    model.addAttribute("coleccion", coleccion);
    return "coleccion";
  }

  @GetMapping("/coleccion/{id}/hechos")
  @ResponseBody
  public List<HechoDTO> hechosDeColeccion(@PathVariable Long id) {
    return coleccionService.obtenerHechosPorColeccionId(id);
  }

  @GetMapping("/editarColeccion/{id}")
  public String editarColeccion(@PathVariable Long id, Model model) {
    ColeccionOutputDTO coleccion = coleccionService.obtenerColeccionPorId(id);
    model.addAttribute("coleccion", coleccion);
    return "editarColeccion";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/nuevaColeccion")
  public String nuevaColeccion(Model model) {
    return "nuevaColeccion";
  }

  @CrossOrigin(origins = "*")
  @GetMapping("/colecciones/fuentes")
  @ResponseBody
  public Mono<List<FuenteOutputDTO>> obtenerFuentes() {
    return webClient.get()
            .uri("/fuentes")
            .retrieve()
            .bodyToFlux(FuenteOutputDTO.class)
            .collectList();
  }

  @CrossOrigin(origins = "*")
  @PostMapping("/colecciones")
  @ResponseBody
  public Mono<ResponseEntity<String>> crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
    return webClient.post()
            .uri("/colecciones")
            .bodyValue(coleccionInputDTO)
            .retrieve()
            .toBodilessEntity()
            .map(response -> ResponseEntity.status(response.getStatusCode()).body(""))
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
  }
}
