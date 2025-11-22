package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.*;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.services.ColeccionService;
import ar.edu.utn.frba.dds.client.dtos.hecho.PaginadoHechoDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
public class ColeccionController {
  private final ColeccionService coleccionService;
  private WebClient webClient;

  @Value("${mapbox.token:}")
  private String mapboxToken;

  @Value("${servicio.agregador}")
  private String agregadorUrl;

  public ColeccionController(ColeccionService coleccionService) {
    this.coleccionService = coleccionService;
  }

  private WebClient getWebClient() {
    if (this.webClient == null) {
      this.webClient = WebClient.builder().baseUrl(agregadorUrl).build();
    }
    return this.webClient;
  }

  @GetMapping("/colecciones")
  public String listarColecciones(Model model) {
    PaginaDTO<ColeccionOutputDTO> pagina = getWebClient().get()
        .uri("/colecciones")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PaginaDTO<ColeccionOutputDTO>>() {})
        .block();
    List<ColeccionOutputDTO> colecciones = pagina != null ? pagina.getContent() : List.of();
    model.addAttribute("colecciones", colecciones);
    return "colecciones";
  }

  @GetMapping("/coleccion/{id}")
  public String verDetalleColeccion(
      @PathVariable Long id,
      Model model,
      @RequestParam(name = "modo", required = false, defaultValue = "irrestricto") String modo,
      @RequestParam(name = "categoria", required = false) String categoria,
      @RequestParam(name = "fechaAcontecimientoInicio", required = false) String fechaAcontecimientoInicio,
      @RequestParam(name = "fechaAcontecimientoFin", required = false) String fechaAcontecimientoFin,
      @RequestParam(name = "titulo", required = false) String titulo,
      @RequestParam(name = "latitud", required = false) String latitud,
      @RequestParam(name = "longitud", required = false) String longitud,
      @RequestParam(name = "lat", required = false) String lat,
      @RequestParam(name = "lng", required = false) String lng,
      @RequestParam(name = "ubicacion", required = false) String ubicacion,
      @RequestParam(name = "fechaCargaInicio", required = false) String fechaCargaInicio,
      @RequestParam(name = "fechaCargaFin", required = false) String fechaCargaFin,
      @RequestParam(name = "fuente", required = false) String fuente,
      @RequestParam(name = "page", required = false, defaultValue = "0") Long page,
      @RequestParam(name = "size", required = false, defaultValue = "10") Long size
  ) {
    ColeccionOutputDTO coleccion = getWebClient().get()
        .uri("/colecciones/" + id)
        .retrieve()
        .bodyToMono(ColeccionOutputDTO.class)
        .block();
    model.addAttribute("coleccion", coleccion);
    model.addAttribute("modo", modo);

    String finalLat = (latitud != null && !latitud.isBlank()) ? latitud : (lat != null && !lat.isBlank() ? lat : null);
    String finalLng = (longitud != null && !longitud.isBlank()) ? longitud : (lng != null && !lng.isBlank() ? lng : null);

    String faIni = fechaAcontecimientoInicio;
    String faFin = fechaAcontecimientoFin;
    String fcIni = fechaCargaInicio;
    String fcFin = fechaCargaFin;

    // la regex funciona asi: \\d{4} → 4 dígitos  || \\d{2} → 2 dígitos
    if (faIni != null && faIni.matches("\\d{4}-\\d{2}-\\d{2}")) {
      faIni = faIni + "T00:00:00";
    }
    if (faFin != null && faFin.matches("\\d{4}-\\d{2}-\\d{2}")) {
      faFin = faFin + "T23:59:59";
    }
    if (fcIni != null && fcIni.matches("\\d{4}-\\d{2}-\\d{2}")) {
      fcIni = fcIni + "T00:00:00";
    }
    if (fcFin != null && fcFin.matches("\\d{4}-\\d{2}-\\d{2}")) {
      fcFin = fcFin + "T23:59:59";
    }

    final String qCategoria = (categoria != null && !categoria.isBlank()) ? categoria : null;
    final String qTitulo = (titulo != null && !titulo.isBlank()) ? titulo : null;
    String qLat = (finalLat != null && !finalLat.isBlank()) ? finalLat : null;
    String qLng = (finalLng != null && !finalLng.isBlank()) ? finalLng : null;
    final String qFaIni = (faIni != null && !faIni.isBlank()) ? faIni : null;
    final String qFaFin = (faFin != null && !faFin.isBlank()) ? faFin : null;
    final String qFcIni = (fcIni != null && !fcIni.isBlank()) ? fcIni : null;
    final String qFcFin = (fcFin != null && !fcFin.isBlank()) ? fcFin : null;
    final String qFuente = (fuente != null && !fuente.isBlank()) ? fuente : null;

    if ((qLat == null || qLng == null) && ubicacion != null && !ubicacion.isBlank() && mapboxToken != null && !mapboxToken.isBlank()) {
      try {
        String q = URLEncoder.encode(ubicacion, StandardCharsets.UTF_8);
        WebClient geo = WebClient.builder().baseUrl("https://api.mapbox.com").build();
        JsonNode geoResp = geo.get()
            .uri(uriBuilder -> uriBuilder.path("/geocoding/v5/mapbox.places/" + q + ".json").queryParam("access_token", mapboxToken).queryParam("limit", 1).queryParam("language", "es").build())
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block();
        if (geoResp != null && geoResp.has("features") && geoResp.get("features").size() > 0) {
          JsonNode first = geoResp.get("features").get(0);
          JsonNode coords = first.path("geometry").path("coordinates");
          if (coords.isArray() && coords.size() >= 2) {
            qLng = coords.get(0).asText();
            qLat = coords.get(1).asText();
            log.info("Geocoding server-side: {} -> {},{}", ubicacion, qLat, qLng);
          }
        }
      } catch (Exception e) {
        log.warn("Error en geocoding server-side para '{}' : {}", ubicacion, e.getMessage());
      }
    }

    final String fqLat = (qLat != null && !qLat.isBlank()) ? qLat : null;
    final String fqLng = (qLng != null && !qLng.isBlank()) ? qLng : null;

    PaginadoHechoDTO paginado;

    if ("curado".equalsIgnoreCase(modo)) {
      // Modo curado: se usa el endpoint /hechos/curados con paginación
      paginado = getWebClient().get()
          .uri(uriBuilder -> {
            var b = uriBuilder.path("/colecciones/" + id + "/hechos/curados")
                .queryParam("page", page)
                .queryParam("size", size);
            if (qCategoria != null) b = b.queryParam("categoria", qCategoria);
            if (qFaIni != null) b = b.queryParam("fechaAcontecimientoInicio", qFaIni);
            if (qFaFin != null) b = b.queryParam("fechaAcontecimientoFin", qFaFin);
            if (qTitulo != null) b = b.queryParam("titulo", qTitulo);
            if (fqLat != null) b = b.queryParam("latitud", fqLat);
            if (fqLng != null) b = b.queryParam("longitud", fqLng);
            if (qFcIni != null) b = b.queryParam("fechaCargaInicio", qFcIni);
            if (qFcFin != null) b = b.queryParam("fechaCargaFin", qFcFin);
            if (qFuente != null) b = b.queryParam("fuente", qFuente);
            return b.build();
          })
          .retrieve()
          .bodyToMono(PaginadoHechoDTO.class)
          .block();
    } else {
      // Modo irrestricto: se usa el endpoint /hechos con paginación
      paginado = getWebClient().get()
          .uri(uriBuilder -> {
            var b = uriBuilder.path("/colecciones/" + id + "/hechos")
                .queryParam("page", page)
                .queryParam("size", size);
            if (qCategoria != null) b = b.queryParam("categoria", qCategoria);
            if (qFaIni != null) b = b.queryParam("fechaAcontecimientoInicio", qFaIni);
            if (qFaFin != null) b = b.queryParam("fechaAcontecimientoFin", qFaFin);
            if (qTitulo != null) b = b.queryParam("titulo", qTitulo);
            if (fqLat != null) b = b.queryParam("latitud", fqLat);
            if (fqLng != null) b = b.queryParam("longitud", fqLng);
            if (qFcIni != null) b = b.queryParam("fechaCargaInicio", qFcIni);
            if (qFcFin != null) b = b.queryParam("fechaCargaFin", qFcFin);
            if (qFuente != null) b = b.queryParam("fuente", qFuente);
            return b.build();
          })
          .retrieve()
          .bodyToMono(PaginadoHechoDTO.class)
          .block();
    }

    if (paginado != null) {
      model.addAttribute("hechos", paginado.getContent());
      model.addAttribute("cantidad", paginado.getTotalElements());
      model.addAttribute("currentPage", paginado.getNumber());
      model.addAttribute("totalPages", paginado.getTotalPages());
    } else {
      model.addAttribute("hechos", List.of());
      model.addAttribute("cantidad", 0);
      model.addAttribute("currentPage", 0);
      model.addAttribute("totalPages", 1);
    }

    return "coleccion";
  }

  @GetMapping("/coleccion/{id}/hechos")
  @ResponseBody
  public List<HechoDTO> hechosDeColeccion(@PathVariable Long id) {
    return coleccionService.obtenerHechosPorColeccionId(id);
  }

  @GetMapping("/editarColeccion/{id}")
  public String editarColeccion(@PathVariable Long id, Model model) {
      ColeccionOutputDTO coleccion = getWebClient().get()
        .uri("/colecciones/" + id)
        .retrieve()
        .bodyToMono(ColeccionOutputDTO.class)
        .block();
    if (coleccion == null) {
      coleccion = new ColeccionOutputDTO();
    }
    if (coleccion.getConsensos() == null) {
      coleccion.setConsensos(java.util.Collections.emptyList());
    }

    List<FuenteOutputDTO> fuentes = getWebClient().get()
            .uri("/fuentes")
            .retrieve()
            .bodyToFlux(FuenteOutputDTO.class)
            .collectList()
            .block();

    model.addAttribute("coleccion", coleccion);
    model.addAttribute("fuentes", fuentes);
     return "editarColeccion";
  }

  @PostMapping("/editarColeccion/{id}")
  public String actualizarColeccion(@PathVariable Long id, @ModelAttribute NuevaColeccionForm form) {
    try {
      CriterioInputDTO criterio = new CriterioInputDTO();
      criterio.setCategoria(form.getCriterioCategoria() != null && !form.getCriterioCategoria().trim().isEmpty()
          ? form.getCriterioCategoria() : null);
      criterio.setTitulo(form.getCriterioTitulo() != null && !form.getCriterioTitulo().trim().isEmpty()
          ? form.getCriterioTitulo() : null);
      criterio.setLatitud(form.getCriterioLatitud() != null && !form.getCriterioLatitud().trim().isEmpty()
          ? form.getCriterioLatitud() : null);
      criterio.setLongitud(form.getCriterioLongitud() != null && !form.getCriterioLongitud().trim().isEmpty()
          ? form.getCriterioLongitud() : null);

      try {
        if (form.getCriterioFechaCargaInicio() != null && !form.getCriterioFechaCargaInicio().isBlank()) {
          criterio.setFechaCargaInicio(java.time.LocalDateTime.parse(form.getCriterioFechaCargaInicio()));
        }
        if (form.getCriterioFechaCargaFin() != null && !form.getCriterioFechaCargaFin().isBlank()) {
          criterio.setFechaCargaFin(java.time.LocalDateTime.parse(form.getCriterioFechaCargaFin()));
        }
        if (form.getCriterioFechaAcontecimientoInicio() != null && !form.getCriterioFechaAcontecimientoInicio().isBlank()) {
          criterio.setFechaAcontecimientoInicio(java.time.LocalDateTime.parse(form.getCriterioFechaAcontecimientoInicio()));
        }
        if (form.getCriterioFechaAcontecimientoFin() != null && !form.getCriterioFechaAcontecimientoFin().isBlank()) {
          criterio.setFechaAcontecimientoFin(java.time.LocalDateTime.parse(form.getCriterioFechaAcontecimientoFin()));
        }
      } catch (Exception e) {
        log.error("Error parseando fechas: {}", e.getMessage());
      }

      ColeccionInputDTO coleccionInputDTO = new ColeccionInputDTO();
      coleccionInputDTO.setNombre(form.getNombre());
      coleccionInputDTO.setDescripcion(form.getDescripcion());
      coleccionInputDTO.setCriterio(criterio);

      if (form.getFuentes() == null || form.getFuentes().isEmpty()) {
          log.error("No se seleccionaron fuentes");
          throw new RuntimeException("Debe seleccionar al menos una fuente");
      }

      List<NombreFuenteInputDTO> fuentesDTO = form.getFuentes().stream().map(f -> {
          NombreFuenteInputDTO dto = new NombreFuenteInputDTO();
          dto.setNombre(f);
          return dto;
      }).toList();
      coleccionInputDTO.setFuentes(fuentesDTO);

      coleccionInputDTO.setConsensos(form.getConsensos());

      getWebClient().put()
          .uri("/colecciones/" + id)
          .bodyValue(coleccionInputDTO)
          .retrieve()
          .toBodilessEntity()
          .block();

      return "redirect:/colecciones";
    } catch (Exception e) {
      log.error("Error actualizando colección: {}", e.getMessage(), e);
      throw e;
    }
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/nuevaColeccion")
  public String nuevaColeccion(Model model) {
    List<FuenteOutputDTO> fuentes = getWebClient().get()
            .uri("/fuentes")
            .retrieve()
            .bodyToFlux(FuenteOutputDTO.class)
            .collectList()
            .block();
    model.addAttribute("fuentes", fuentes);
    return "nuevaColeccion";
  }

  @PostMapping("/nuevaColeccion")
  public String crearColeccion(@ModelAttribute NuevaColeccionForm form) {
    try {
      CriterioInputDTO criterio = new CriterioInputDTO();
      criterio.setCategoria(form.getCriterioCategoria() != null && !form.getCriterioCategoria().trim().isEmpty()
          ? form.getCriterioCategoria() : null);
      criterio.setTitulo(form.getCriterioTitulo() != null && !form.getCriterioTitulo().trim().isEmpty()
          ? form.getCriterioTitulo() : null);
      criterio.setLatitud(form.getCriterioLatitud() != null && !form.getCriterioLatitud().trim().isEmpty()
          ? form.getCriterioLatitud() : null);
      criterio.setLongitud(form.getCriterioLongitud() != null && !form.getCriterioLongitud().trim().isEmpty()
          ? form.getCriterioLongitud() : null);

      try {
        if (form.getCriterioFechaCargaInicio() != null && !form.getCriterioFechaCargaInicio().isBlank()) {
          criterio.setFechaCargaInicio(java.time.LocalDateTime.parse(form.getCriterioFechaCargaInicio()));
        }
        if (form.getCriterioFechaCargaFin() != null && !form.getCriterioFechaCargaFin().isBlank()) {
          criterio.setFechaCargaFin(java.time.LocalDateTime.parse(form.getCriterioFechaCargaFin()));
        }
        if (form.getCriterioFechaAcontecimientoInicio() != null && !form.getCriterioFechaAcontecimientoInicio().isBlank()) {
          criterio.setFechaAcontecimientoInicio(java.time.LocalDateTime.parse(form.getCriterioFechaAcontecimientoInicio()));
        }
        if (form.getCriterioFechaAcontecimientoFin() != null && !form.getCriterioFechaAcontecimientoFin().isBlank()) {
          criterio.setFechaAcontecimientoFin(java.time.LocalDateTime.parse(form.getCriterioFechaAcontecimientoFin()));
        }
      } catch (Exception e) {
        log.error("Error parseando fechas: {}", e.getMessage());
      }

      ColeccionInputDTO coleccionInputDTO = new ColeccionInputDTO();
      coleccionInputDTO.setNombre(form.getNombre());
      coleccionInputDTO.setDescripcion(form.getDescripcion());
      coleccionInputDTO.setCriterio(criterio);

      if (form.getFuentes() == null || form.getFuentes().isEmpty()) {
          log.error("No se seleccionaron fuentes");
          throw new RuntimeException("Debe seleccionar al menos una fuente");
      }

      List<NombreFuenteInputDTO> fuentesDTO = form.getFuentes().stream().map(f -> {
          NombreFuenteInputDTO dto = new NombreFuenteInputDTO();
          dto.setNombre(f);
          return dto;
      }).toList();
      coleccionInputDTO.setFuentes(fuentesDTO);

      coleccionInputDTO.setConsensos(form.getConsensos());

      getWebClient().post()
          .uri("/colecciones")
          .bodyValue(coleccionInputDTO)
          .retrieve()
          .toBodilessEntity()
          .block();

      return "redirect:/colecciones";
    } catch (Exception e) {
      log.error("Error creando colección: {}", e.getMessage(), e);
      throw e;
    }
  }

  @CrossOrigin(origins = "*")
  @GetMapping("/colecciones/fuentes")
  @ResponseBody
  public Mono<List<FuenteOutputDTO>> obtenerFuentes() {
    return getWebClient().get()
            .uri("/fuentes")
            .retrieve()
            .bodyToFlux(FuenteOutputDTO.class)
            .collectList();
  }
}
