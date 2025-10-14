package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.FuenteDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.solicitud.SolicitudDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
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
    private final DinamicaService dinamicaService;

    public PanelControlController(HechoService hechoService, FuenteService fuenteService, SolicitudesService solicitudesService, DinamicaService dinamicaService) {
        this.hechoService = hechoService;
        this.fuenteService = fuenteService;
        this.solicitudesService = solicitudesService;
        this.dinamicaService = dinamicaService;
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
        List<HechoDTO> hechos = this.dinamicaService.obtenerHechosPendientes();
        model.addAttribute("titulo", "Hechos Pendientes");
        model.addAttribute("hechos", hechos);
        model.addAttribute("cantidadHechos", hechos.size());
        return "panelControl/hechosPendientes";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/fuentes")
    public String panelControlFuentes(Model model) {
        List<FuenteDTO> fuentes = this.fuenteService.obtenerFuentesNuevas();
        model.addAttribute("titulo", "Fuentes Nuevas");
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("cantidadFuentes", fuentes.size());
        return "panelControl/fuentes";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/solicitudes")
    public String panelControlSolicitudes(Model model) {
        List<SolicitudDTO> solicitudes = this.solicitudesService.obtenerSolicitudes();
        model.addAttribute("titulo", "Solicitudes");
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("cantidadSolicitudes", solicitudes.stream().filter(s -> s.getEstado().equals("PENDIENTE")).count());
        return "panelControl/solicitudes";
    }
}
