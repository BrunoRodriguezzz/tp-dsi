package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.FuenteDTO;
import ar.edu.utn.frba.dds.client.dtos.SolicitudCambiosDTO;
import ar.edu.utn.frba.dds.client.dtos.SolicitudEliminacionConHechoDTO;
import ar.edu.utn.frba.dds.client.dtos.UsuarioLogueadoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import ar.edu.utn.frba.dds.client.services.FuenteService;
import ar.edu.utn.frba.dds.client.services.HechoService;
import ar.edu.utn.frba.dds.client.services.SolicitudesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/panelControl")
@Controller
public class PanelControlController {
  private final HechoService hechoService;
  private final FuenteService fuenteService;
  private final SolicitudesService solicitudesService;
  private final DinamicaService dinamicaService;

  public PanelControlController(HechoService hechoService, FuenteService fuenteService, SolicitudesService solicitudesService, DinamicaService dinamicaService) {
    this.hechoService = hechoService;
    this.fuenteService = fuenteService;
    this.solicitudesService = solicitudesService;
    this.dinamicaService = dinamicaService;
  }

  @GetMapping
  public String panelControl(Model model) {
    model.addAttribute("cantidadHechosPendientes", dinamicaService.obtenerHechosPendientes().size());
    model.addAttribute("titulo", "Panel de Control");
    model.addAttribute("cantidadHechos", hechoService.obtenerHechosAgregador().getTotalElements());
    model.addAttribute("cantModificaciones", fuenteService.obtenerFuentesNuevas().size());

    try {
      List<SolicitudEliminacionConHechoDTO> solicitudes = solicitudesService.obtenerSolicitudesEliminacion();
      model.addAttribute("cantidadSolicitudes", solicitudes.stream().filter(s -> "PENDIENTE".equalsIgnoreCase(s.getEstado())).count());
    } catch (Exception e) {
      model.addAttribute("cantidadSolicitudes", 0);
    }

    return "panelControl/index";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/hechosPendientes")
  public String panelControlHechos(Model model) {
    List<HechoDTO> hechos = this.dinamicaService.obtenerHechosPendientes();
    model.addAttribute("titulo", "Hechos Pendientes");
    model.addAttribute("hechos", hechos);
    model.addAttribute("cantidadHechos", hechos.size());
    return "panelControl/hechosPendientes";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/solicitudesModificado")
  public String panelControlModificados(Model model) {
    List<SolicitudCambiosDTO> solicitudes = this.dinamicaService.obtenerSolicitudesCambios();
    model.addAttribute("titulo", "Solicitudes de Modificación");
    model.addAttribute("solicitudes", solicitudes);
    model.addAttribute("cantidadSolicitudes", solicitudes.size());
    return "panelControl/solicitudesModificado";
  }

  @GetMapping("/importarCSV")
  public String panelControlImportarCSV(Model model) {
    model.addAttribute("titulo", "Importar CSV");
    return "panelControl/importarCSV";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @GetMapping("/solicitudes")
  public String panelControlSolicitudes(Model model, Authentication authentication) {
    try {
      List<SolicitudEliminacionConHechoDTO> solicitudes = solicitudesService.obtenerSolicitudesEliminacion();
      model.addAttribute("titulo", "Solicitudes de Eliminación");
      model.addAttribute("solicitudes", solicitudes);
      model.addAttribute("cantidadSolicitudes", solicitudes.stream().filter(s -> "PENDIENTE".equalsIgnoreCase(s.getEstado())).count());

      String username = authentication.getName();
      UsuarioLogueadoDTO usuarioLogueado = new UsuarioLogueadoDTO("Mariana", "Perez", username);
      model.addAttribute("usuarioLogueado", usuarioLogueado);

    } catch (Exception e) {
      model.addAttribute("error", "Error al cargar las solicitudes: " + e.getMessage());
      model.addAttribute("solicitudes", List.of());
      model.addAttribute("cantidadSolicitudes", 0);

      String username = authentication.getName();
      UsuarioLogueadoDTO usuarioLogueado = new UsuarioLogueadoDTO("Mariana", "Perez", username);
      model.addAttribute("usuarioLogueado", usuarioLogueado);
    }
    return "panelControl/solicitudes";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/solicitudes/{id}/aceptar")
  public String aceptarSolicitud(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
      String username = authentication.getName();
      solicitudesService.aceptarSolicitud(id, "Mariana", "Perez");
      redirectAttributes.addFlashAttribute("mensaje", "Solicitud aceptada correctamente");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al aceptar la solicitud: " + e.getMessage());
    }
    return "redirect:/panelControl/solicitudes";
  }

  @PreAuthorize("hasRole('ADMINISTRADOR')")
  @PostMapping("/solicitudes/{id}/rechazar")
  public String rechazarSolicitud(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
      String username = authentication.getName();
      solicitudesService.rechazarSolicitud(id, "Mariana", "Perez");
      redirectAttributes.addFlashAttribute("mensaje", "Solicitud rechazada correctamente");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al rechazar la solicitud: " + e.getMessage());
    }
    return "redirect:/panelControl/solicitudes";
  }
}

