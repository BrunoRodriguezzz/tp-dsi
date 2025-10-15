package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.*;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.services.ColeccionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.LoggingEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ColeccionController {
  private final ColeccionService coleccionService;
  private final WebClient webClient;
  private final String agregadorUrl;

  public ColeccionController(ColeccionService coleccionService,
                             @Value("http://localhost:8082") String agregadorUrl) {
    this.coleccionService = coleccionService;
    this.agregadorUrl = agregadorUrl;
    this.webClient = WebClient.builder().baseUrl(agregadorUrl).build();
  }

  @GetMapping("/colecciones")
  public String listarColecciones(Model model) {
    PaginaDTO<ColeccionOutputDTO> pagina = webClient.get()
        .uri("/colecciones")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PaginaDTO<ColeccionOutputDTO>>() {})
        .block();
    List<ColeccionOutputDTO> colecciones = pagina != null ? pagina.getContent() : List.of();
    log.info("Colecciones recibidas del agregador: {}", colecciones);
    model.addAttribute("colecciones", colecciones);
    return "colecciones";
  }

  @GetMapping("/coleccion/{id}")
  public String verDetalleColeccion(@PathVariable Long id, Model model) {
    ColeccionOutputDTO coleccion = webClient.get()
        .uri("/colecciones/" + id)
        .retrieve()
        .bodyToMono(ColeccionOutputDTO.class)
        .block();
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
    List<FuenteOutputDTO> fuentes = webClient.get()
            .uri("/fuentes")
            .retrieve()
            .bodyToFlux(FuenteOutputDTO.class)
            .collectList()
            .block();
    model.addAttribute("fuentes", fuentes);
    return "nuevaColeccion";
  }

  @PostMapping("/nuevaColeccion")
  public String crearColeccion(@RequestParam String titulo,
                            @RequestParam String descripcion,
                            @RequestParam(required = false) List<String> fuentes,
                            @ModelAttribute CriterioInputDTO criterio,
                            @RequestParam(required = false) List<String> consensos) {
    ColeccionInputDTO coleccionInputDTO = new ColeccionInputDTO();
    coleccionInputDTO.setTitulo(titulo);
    coleccionInputDTO.setDescripcion(descripcion);
    coleccionInputDTO.setCriterio(criterio);
    if (fuentes != null) {
        List<NombreFuenteInputDTO> fuentesDTO = fuentes.stream().map(f -> {
            NombreFuenteInputDTO dto = new NombreFuenteInputDTO();
            dto.setNombre(f);
            return dto;
        }).toList();
        coleccionInputDTO.setFuentes(fuentesDTO);
    }
    coleccionInputDTO.setConsensos(consensos);
    log.info("Informacion que vamos a mandar: {}", coleccionInputDTO);
    webClient.post()
        .uri("/colecciones")
        .bodyValue(coleccionInputDTO)
        .retrieve()
        .toBodilessEntity()
        .block();
    return "redirect:/colecciones";
  }

  @CrossOrigin(origins = "*")
  @GetMapping("/colecciones/fuentes")
  @ResponseBody
  public Mono<List<FuenteOutputDTO>> obtenerFuentes() {
    System.out.println("Obteniendo fuentessssssssssssss");
    return webClient.get()
            .uri("/fuentes")
            .retrieve()
            .bodyToFlux(FuenteOutputDTO.class)
            .collectList();
  }
}
