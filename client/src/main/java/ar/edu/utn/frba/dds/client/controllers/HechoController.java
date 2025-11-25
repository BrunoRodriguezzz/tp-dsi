package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.Params;
import ar.edu.utn.frba.dds.client.dtos.SolicitudEliminacionConHechoDTO;
import ar.edu.utn.frba.dds.client.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoRevisadoForm;
import ar.edu.utn.frba.dds.client.dtos.hecho.PaginadoHechoDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import ar.edu.utn.frba.dds.client.services.HechoService;
import ar.edu.utn.frba.dds.client.services.internal.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechoController {
  private final HechoService hechoService;
  private final DinamicaService dinamicaService;
  private final StorageService  storageService;
  private final Logger LOGGER = LogManager.getLogger(HechoController.class);

  @GetMapping
  public String listarHechos(
      Model model,
      @RequestParam(name = "fechaAcontecimientoInicio", required = false) LocalDate fechaAcontecimientoInicio,
      @RequestParam(name = "fechaAcontecimientoFin", required = false) LocalDate fechaAcontecimientoFin,
      @RequestParam(name = "categoria", required = false) String categoria,
      @RequestParam(name = "lat", required = false) Double lat,
      @RequestParam(name = "lng", required = false) Double lon,
      @RequestParam(name = "fuente", required = false) String fuente,
      @RequestParam(name = "page", required = false, defaultValue = "0") Long page,
      @RequestParam(name = "size", required = false, defaultValue = "10") Long size
  ) {
    Params params = new Params(fechaAcontecimientoInicio, fechaAcontecimientoFin, categoria, lat, lon, fuente, page, size);
    model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");
    PaginadoHechoDTO paginado = this.hechoService.obtenerHechosAgregadorFiltrados(params);
    if (paginado != null) {
      model.addAttribute("hechos", paginado.getContent());
      model.addAttribute("cantidad", paginado.getTotalElements());
      model.addAttribute("currentPage", paginado.getNumber());
      model.addAttribute("totalPages", paginado.getTotalPages());
      model.addAttribute("size", paginado.getSize());
    } else {
      model.addAttribute("hechos", List.of());
      model.addAttribute("cantidad", 0);
      model.addAttribute("currentPage", 0);
      model.addAttribute("totalPages", 0);
      model.addAttribute("size", size);
    }
    return "hechos";
  }


  @GetMapping("/{id}")
  public String verDetalleHecho(@PathVariable Long id, Model model) {
    HechoDTO hecho = hechoService.obtenerHechoPorId(id);
    model.addAttribute("hecho", hecho);
    model.addAttribute("titulo", hecho.getTitulo());
    return "hecho";
  }

  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CONTRIBUYENTE')")
  @GetMapping("/contribuyentes/{id}")
  public String verDetalleHechoContribuyente(@PathVariable Long id, Model model) {
    HechoDTO hecho = dinamicaService.buscarHechoId(id);
    log.info("Contenido Multimedia: {}", hecho.getContenidoMultimedia());
    model.addAttribute("hecho", hecho);
    model.addAttribute("titulo", hecho.getTitulo());
    return "hecho";
  }

  @GetMapping("/verDetalle/{id}")
  public String verDetalleHechoNuevo(@PathVariable Long id, Model model) { // TODO: Ver para q sirve
    HechoDTO hecho = hechoService.obtenerHechoPorId(id);
    LOGGER.info("Mostrando {} hecho.", id);
    model.addAttribute("hecho", hecho);
    model.addAttribute("titulo", hecho.getTitulo());
    return "verDetalle";
  }

  @PreAuthorize("hasAnyRole('ADMINISTRADOR','CONTRIBUYENTE')")
  @GetMapping("/misHechos")
  public String mostrarMisHechos(@SessionAttribute("id") Long id, Model model){
    List<HechoDTO> hechos = this.dinamicaService.mostrarMisHechos(id);

      Map<Long, Long> diasRestantesMap = new HashMap<>();

      LocalDate hoy = LocalDate.now();
      for (HechoDTO hecho : hechos) {
          LocalDateTime fechaCarga = hecho.getFechaCarga();
          if (fechaCarga != null) {
              long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaCarga.plusDays(7));
              if (diasRestantes < 0) diasRestantes = 0;
              diasRestantesMap.put(hecho.getId(), diasRestantes);
          } else {
              diasRestantesMap.put(hecho.getId(), 0L);
          }
      }

    model.addAttribute("hechos", hechos);
    model.addAttribute("cantidad", hechos.size());
    model.addAttribute("diasRestantesMap", diasRestantesMap);
    model.addAttribute("titulo", "Mis Hechos");
    return "misHechos";
  }

  @GetMapping("/eliminacion/{id}")
  public String eliminacionHecho(@PathVariable Long id, Model model){
    HechoDTO hecho = hechoService.obtenerHechoPorId(id);
    model.addAttribute("hecho", hecho);
    model.addAttribute("idHecho", id);
    model.addAttribute("titulo", "Eliminación");
    return "eliminarHecho";
  }

  @PostMapping("/eliminacion/{id}")
  public String procesarEliminacion(@PathVariable Long id,
                                    @RequestParam String fundamento,
                                    Model model,
                                    RedirectAttributes redirect) {

    // Validar longitud del fundamento
    if (fundamento == null || fundamento.trim().length() < 500) {
      // Mantener el fundamento y otros datos para que no se pierdan
      HechoDTO hecho = hechoService.obtenerHechoPorId(id);
      model.addAttribute("hecho", hecho);
      model.addAttribute("idHecho", id);
      model.addAttribute("titulo", "Eliminación");
      model.addAttribute("fundamento", fundamento); // Mantener el texto escrito
      model.addAttribute("error",
          "El fundamento debe tener al menos 500 caracteres. Actualmente tiene: " +
              (fundamento != null ? fundamento.length() : 0));
      return "eliminarHecho";
    }

    try {
      SolicitudEliminacionDTO solicitud = new SolicitudEliminacionDTO();
      solicitud.setIdHecho(id);
      solicitud.setFundamento(fundamento.trim());
      solicitud.setFechaCreacion(LocalDateTime.now());
      solicitud.setIdContribuyente(null);

      SolicitudEliminacionConHechoDTO exito = hechoService.enviarSolicitudEliminacion(solicitud);

      if (exito != null && !exito.getEstado().equals("SPAM")) {
        redirect.addFlashAttribute("exito", "Solicitud de eliminación enviada correctamente.");
        return "redirect:/hechos/misHechos";
      }
      if (exito != null && exito.getEstado().equals("SPAM")) {
        HechoDTO hecho = hechoService.obtenerHechoPorId(id);
        model.addAttribute("hecho", hecho);
        model.addAttribute("idHecho", id);
        model.addAttribute("titulo", "Eliminación");
        model.addAttribute("fundamento", fundamento);
        model.addAttribute("error", "Su solicitud fue detectada como SPAM");
        return "eliminarHecho";
      }
      else {
        HechoDTO hecho = hechoService.obtenerHechoPorId(id);
        model.addAttribute("hecho", hecho);
        model.addAttribute("idHecho", id);
        model.addAttribute("titulo", "Eliminación");
        model.addAttribute("fundamento", fundamento);
        model.addAttribute("error", "Error al enviar la solicitud de eliminación.");
        return "eliminarHecho";
      }

    } catch (Exception e) {
      LOGGER.error("Error al procesar solicitud de eliminación: {}", e.getMessage(), e);
      // Mantener los datos en caso de excepción
      HechoDTO hecho = hechoService.obtenerHechoPorId(id);
      model.addAttribute("hecho", hecho);
      model.addAttribute("idHecho", id);
      model.addAttribute("titulo", "Eliminación");
      model.addAttribute("fundamento", fundamento);
      model.addAttribute("error", "Error interno al procesar la solicitud.");
      return "eliminarHecho";
    }
  }

  @PostMapping("/reporte")
  public String reportarHecho(@RequestParam Long id,
                              @RequestParam String motivo,
                              @RequestParam String descripcion,
                              @RequestParam String contacto,
                              Model model){

    LOGGER.info("ID del hecho a reportar: {}", id);
    LOGGER.info("Motivo: {}", motivo);
    LOGGER.info("Descripcion: {}", descripcion);
    LOGGER.info("Contacto: {}", contacto);

    return "redirect:/hechos";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/gestion")
  public String gestionarHecho(@Valid @ModelAttribute HechoRevisadoForm form) {
    this.dinamicaService.gestionarHecho(form);
    return "redirect:/panelControl/hechosPendientes";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/modificacion")
  public String aceptarModificacion(@Valid @ModelAttribute HechoRevisadoForm form) {
    this.dinamicaService.modificar(form);
    return "redirect:/panelControl";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/pendientes/{id}")
  public String verDetalleHechoPendiente(@PathVariable Long id, Model model) {
    HechoDTO hecho = this.dinamicaService.buscarPendienteID(id);

    if (hecho != null) {
      model.addAttribute("hecho", hecho);
      model.addAttribute("titulo", hecho.getTitulo());
      return "hecho";
    } else {
      return "redirect:/panelControl";
    }
  }

  @GetMapping("/reportarHecho")
  public String mostrarFormulario(Model model) {
    return "eliminarHecho";
  }

  @PostMapping("/reportarHecho")
  public String procesarFormulario() {
    return "redirect:/";
  }

  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CONTRIBUYENTE')")
  @GetMapping("/modificarHecho/{id}")
  public String modificacionHecho(@PathVariable(name = "id") Long id, Model model){
    HechoDTO hecho = hechoService.obtenerHechoPorId(id);
    model.addAttribute("hecho", hecho);
    model.addAttribute("titulo", "Modificacion");
    return "modificarHecho";
  }

  @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
  @GetMapping("/aceptarConSugerencia/{id}")
  public String aceptarConModificacionPantalla(@PathVariable(name = "id") String id, Model model){
    Long idL = Long.parseLong(id);
    HechoDTO hecho = dinamicaService.buscarHechoId(idL);
    model.addAttribute("hecho", hecho);
    model.addAttribute("titulo", "Aceptar con Sugerencia");
    return "aceptarConSugerencia";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/gestionConSugerencia")
  public String gestionConSugerencia(@Valid @ModelAttribute HechoRevisadoForm form) {
    this.dinamicaService.gestionarHecho(form);
    return "redirect:/panelControl/hechosPendientes";
  }

  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CONTRIBUYENTE')")
  @GetMapping("modificarHecho/contribuyentes/{id}")
  public String modificarHechoContribuyente(@PathVariable(name = "id") Long id, @SessionAttribute("id") Long idUsuario, Model model){
    HechoDTO hecho = dinamicaService.buscarHechoId(id);
    model.addAttribute("hecho", hecho);
    model.addAttribute("titulo", "Modificacion");
    model.addAttribute("idUsuario", idUsuario);
    return "modificarHecho";
  }


  @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CONTRIBUYENTE')")
  @PostMapping("modificarHecho/{id}")
  public String procesarModificacionHecho(@PathVariable Long id,
                                          @ModelAttribute HechoDTO hechoDTO,
                                          @RequestParam(value = "nuevosArchivos", required = false) MultipartFile[] nuevosArchivos,
                                          @RequestParam(value = "eliminados", required = false) List<String> eliminados,
                                          RedirectAttributes redirectAttributes) {

      List<String> original = new ArrayList<>();
      if (hechoDTO.getContenidoMultimedia() != null) {
          original.addAll(hechoDTO.getContenidoMultimedia());
      }

      if (eliminados != null) {
          original.removeAll(eliminados);
      }

      if (nuevosArchivos != null) {
          for (MultipartFile file : nuevosArchivos) {
              if (!file.isEmpty()) {
                  String nombreGuardado = this.storageService.store(file);
                  original.add(nombreGuardado);
              }
          }
      }

    hechoDTO.setContenidoMultimedia(original);

    boolean rta;
    if (hechoDTO.getOrigen() != null && hechoDTO.getOrigen().equals("CONTRIBUYENTE")) {
      rta = this.dinamicaService.modificarHecho(hechoDTO);
    }
    else {
      rta = this.hechoService.modificarHecho(id, hechoDTO);
    }
    redirectAttributes.addFlashAttribute("operacion", rta);
    return "redirect:/hechos/misHechos";
  }
}