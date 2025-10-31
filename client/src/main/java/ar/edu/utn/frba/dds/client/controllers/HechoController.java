package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.Params;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoRevisadoForm;
import ar.edu.utn.frba.dds.client.dtos.hecho.PaginadoHechoDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import ar.edu.utn.frba.dds.client.services.HechoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechoController {
    private final HechoService hechoService;
    private final DinamicaService dinamicaService;
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
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "size", required = false) Long size
    ) {
        Params params = new Params(fechaAcontecimientoInicio, fechaAcontecimientoFin, categoria, lat, lon, fuente, page, size);
        model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");
        PaginadoHechoDTO paginado = this.hechoService.obtenerHechosAgregadorFiltrados(params);
        if (paginado != null) {
            model.addAttribute("hechos", paginado.getContent());
            model.addAttribute("cantidad", paginado.getTotalElements());
            LOGGER.info("Mostrando {} hechos.", paginado.getTotalElements());
        } else {
            model.addAttribute("hechos", List.of());
            model.addAttribute("cantidad", 0);
            LOGGER.info("No se encontraron hechos.");
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
        return "redirect:/panelControl/hechosPendientes";
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
        return "reportarHecho";
    }

    @PostMapping("/reportarHecho")
    public String procesarFormulario() {
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CONTRIBUYENTE')")
    @GetMapping("modificarHecho/{id}")
    public String modificacionHecho(@PathVariable(name = "id") Long id, Model model){
        HechoDTO hecho = hechoService.obtenerHechoPorId(id);
        model.addAttribute("hecho", hecho);
        model.addAttribute("titulo", "Modificacion");
        return "modificarHecho";
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
    public String procesarModificacionHecho(@PathVariable Long id, @ModelAttribute HechoDTO hechoDTO, RedirectAttributes redirectAttributes) {
        boolean rta;
        log.info("El ID del usuario que se envia es: {}", hechoDTO.getContribuyente().getId());
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
