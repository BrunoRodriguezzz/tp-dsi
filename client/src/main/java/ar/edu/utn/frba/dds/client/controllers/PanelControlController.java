package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.FuenteDTO;
import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.solicitud.SolicitudDTO;
import ar.edu.utn.frba.dds.client.services.FuenteService;
import ar.edu.utn.frba.dds.client.services.HechoService;
import ar.edu.utn.frba.dds.client.services.SolicitudesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/panelControl")
@Controller
public class PanelControlController {
    private final HechoService hechoService;
    private final FuenteService fuenteService;
    private final SolicitudesService solicitudesService;

    public PanelControlController(HechoService hechoService, FuenteService fuenteService, SolicitudesService solicitudesService) {
        this.hechoService = hechoService;
        this.fuenteService = fuenteService;
        this.solicitudesService = solicitudesService;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public String panelControl(Model model) {
        // TODO: Conseguir hechos pendientes de la dinámica
        model.addAttribute("titulo", "Panel de Control");
        model.addAttribute("cantidadHechos", hechoService.obtenerHechos().size());
        model.addAttribute("cantidadFuentes", fuenteService.obtenerFuentesNuevas().size());
        model.addAttribute("cantidadSolicitudes", solicitudesService.obtenerSolicitudes().size());
        return "panelControl/index";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/hechosPendientes")
    public String panelControlHechos(Model model) {
        List<HechoDTO> hechos = this.hechoService.obtenerHechosPendientes();
        model.addAttribute("titulo", "Hechos Pendientes");
        model.addAttribute("hechos", hechos);
        model.addAttribute("cantidadHechos", hechos.size());
        return "panelControl/hechosPendientes";
    }

    @GetMapping("/fuentes")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String panelControlFuentes(Model model) {
        List<FuenteDTO> fuentes = this.fuenteService.obtenerFuentesNuevas();
        model.addAttribute("titulo", "Fuentes Nuevas");
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("cantidadFuentes", fuentes.size());
        return "panelControl/fuentes";
    }

    @GetMapping("/solicitudes")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String panelControlSolicitudes(Model model) {
        List<SolicitudDTO> solicitudes = this.solicitudesService.obtenerSolicitudes();
        model.addAttribute("titulo", "Solicitudes");
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("cantidadSolicitudes", solicitudes.stream().filter(s -> s.getEstado().equals("PENDIENTE")).count());
        return "panelControl/solicitudes";
    }
}
