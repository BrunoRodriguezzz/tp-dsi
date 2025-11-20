package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoFormDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import ar.edu.utn.frba.dds.client.services.internal.StorageService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/publicarHecho")
@RequiredArgsConstructor
public class PublicarHechoController {

    private final DinamicaService dinamicaService;
    private final StorageService storageService;
    private final Logger LOGGER = LogManager.getLogger(PublicarHechoController.class);

    @GetMapping
    public String publicacionHecho(Model model){
        model.addAttribute("titulo", "Publicar Hecho");
        return "publicarHecho";
    }

    @PostMapping
    public String publicarHecho(@ModelAttribute HechoFormDTO hecho, RedirectAttributes redirect) {
        // Validaciones mínimas del lado cliente-servidor con detalle de campos faltantes
        List<String> faltantes = getStrings(hecho);

        if (!faltantes.isEmpty()) {
            redirect.addFlashAttribute("error", "Faltan datos obligatorios: " + String.join(", ", faltantes));
            return "redirect:/publicarHecho";
        }

        List<String> pathMultimedia = new ArrayList<>();
        if (hecho.getContenidoMultimedia() != null && !hecho.getContenidoMultimedia().isEmpty()) {
            try {
                for (var archivo : hecho.getContenidoMultimedia()) {
                    String path = this.storageService.store(archivo);
                    pathMultimedia.add(path);
                }
            } catch (Exception e) {
                LOGGER.error("Fallo al guardar archivos multimedia: {}", e.getMessage(), e);
                redirect.addFlashAttribute("error", "Fallo al guardar archivos multimedia: " + e.getMessage());
                pathMultimedia.forEach(this.storageService::delete);
                return "redirect:/publicarHecho";
            }
        }

        HechoInputDTO hechoInputDTO = HechoInputDTO.fromHechoFormDTO(hecho, pathMultimedia);

        try {
            this.dinamicaService.enviarHecho(hechoInputDTO);
            redirect.addFlashAttribute("exito", "Hecho enviado para revisión.");
            return "redirect:/hechos";
        } catch (Exception e) {
            LOGGER.error("Fallo al enviar hecho: {}", e.getMessage(), e);
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/publicarHecho";
        }
    }

    private static List<String> getStrings(HechoFormDTO hecho) {
        List<String> faltantes = new ArrayList<>();
        if (hecho.getTitulo() == null || hecho.getTitulo().isBlank()) faltantes.add("Título");
        if (hecho.getDescripcion() == null || hecho.getDescripcion().isBlank()) faltantes.add("Descripción");
        if (hecho.getCategoria() == null || hecho.getCategoria().isBlank()) faltantes.add("Categoría");
        if (hecho.getLatitud() == null || hecho.getLongitud() == null) faltantes.add("Ubicación (lat/long)");
        if (hecho.getMunicipio() == null || hecho.getMunicipio().isBlank()) faltantes.add("Municipio");
        if (hecho.getProvincia() == null || hecho.getProvincia().isBlank()) faltantes.add("Provincia");
        if (hecho.getFechaAcontecimiento() == null) faltantes.add("Fecha de Acontecimiento");
        // if (hecho.getContenidoMultimedia().isEmpty()) faltantes.add("Multimedia"); opcional
        return faltantes;
    }
}
