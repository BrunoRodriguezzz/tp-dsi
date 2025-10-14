package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoRevisadoForm;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import ar.edu.utn.frba.dds.client.services.HechoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechoController {
    private final HechoService hechoService;
    private final DinamicaService dinamicaService;
    private final Logger LOGGER = LogManager.getLogger(HechoController.class);

    @GetMapping
    public String listarHechos(Model model) {
        model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");
        List<HechoDTO> hechos = this.hechoService.obtenerHechos();
        model.addAttribute("hechos", hechos);
        model.addAttribute("cantidad", hechos.size());
        LOGGER.info("Mostrando {} hechos.", hechos.size());
        return "hechos";
    }

    @GetMapping("/{id}")
    public String verDetalleHecho(@PathVariable Long id, Model model) {
        HechoDTO hecho = hechoService.obtenerHechoPorId(id);
        LOGGER.info("Mostrando {} hecho.", id);
        model.addAttribute("hecho", hecho);
        model.addAttribute("titulo", hecho.getTitulo());
        return "hecho";
    }

    @GetMapping("/verDetalle/{id}")
    public String verDetalleHechoNuevo(@PathVariable Long id, Model model) {
        HechoDTO hecho = hechoService.obtenerHechoPorId(id);
        LOGGER.info("Mostrando {} hecho.", id);
        model.addAttribute("hecho", hecho);
        model.addAttribute("titulo", hecho.getTitulo());
        return "verDetalle";
    }

    @GetMapping("/misHechos")
    public String mostrarMisHechos(@RequestParam(required = false) Long id, Model model){
        List<HechoDTO> hechos = this.dinamicaService.mostrarMisHechos(id);
        hechos.forEach(h -> LOGGER.info("Mostrando estado del hecho: {}.", h.getEstado()));
        model.addAttribute("hechos", hechos);
        model.addAttribute("cantidad", hechos.size());
        model.addAttribute("titulo", "Mis Hechos");
        return "misHechos";
    }

    @GetMapping("/eliminacion/{id}")
    public String eliminacionHecho(@PathVariable Long id, Model model){
        HechoDTO hecho = hechoService.obtenerHechoPorId(id);
        model.addAttribute("hecho", hecho);
        model.addAttribute("titulo", "Eliminación");
        return "reportarHecho";
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
        LOGGER.info("Hecho {} gestionado por el administrador {}.", form.getId(), form.getIdAdministrador());
        return "redirect:/panelControl/hechosPendientes";
    }
}
