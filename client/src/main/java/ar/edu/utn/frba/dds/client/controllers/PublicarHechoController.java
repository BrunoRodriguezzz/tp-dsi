package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/publicarHecho")
@RequiredArgsConstructor
public class PublicarHechoController {

    private final DinamicaService dinamicaService;
    private final Logger LOGGER = LogManager.getLogger(PublicarHechoController.class);
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";
    @GetMapping
    public String publicacionHecho(Model model){
        model.addAttribute("titulo", "Publicar Hecho");
        return "publicarHecho";
    }

    @PostMapping
    public String publicarHecho(@ModelAttribute HechoInputDTO hecho,
                                @RequestParam(value = "archivo") MultipartFile[] archivos,
                                RedirectAttributes redirect) {

        LOGGER.info("ID del usuario: {}", hecho.getIdUsuario());

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        List<String> mult = new ArrayList<>();

        try {
            for (MultipartFile archivo : archivos) {
                if (archivo.isEmpty()) continue;

                String filename = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();

                Path filePath = Paths.get(UPLOAD_DIR, filename);
                Files.write(filePath, archivo.getBytes());

                mult.add("/uploads/" + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Error al guardar archivo multimedia");
            return "redirect:/publicarHecho";
        }

        hecho.setContenidoMultimedia(mult);

        // Validaciones mínimas del lado cliente-servidor con detalle de campos faltantes
        List<String> faltantes = getStrings(hecho);

        if (!faltantes.isEmpty()) {
            redirect.addFlashAttribute("error", "Faltan datos obligatorios: " + String.join(", ", faltantes));
            return "redirect:/publicarHecho";
        }

        try {
            this.dinamicaService.enviarHecho(hecho);
            redirect.addFlashAttribute("exito", "Hecho enviado para revisión.");
            return "redirect:/hechos";
        } catch (Exception e) {
            LOGGER.error("Fallo al enviar hecho: {}", e.getMessage(), e);
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/publicarHecho";
        }
    }

    private static List<String> getStrings(HechoInputDTO hecho) {
        List<String> faltantes = new ArrayList<>();
        if (hecho.getTitulo() == null || hecho.getTitulo().isBlank()) faltantes.add("Título");
        if (hecho.getDescripcion() == null || hecho.getDescripcion().isBlank()) faltantes.add("Descripción");
        if (hecho.getCategoria() == null || hecho.getCategoria().isBlank()) faltantes.add("Categoría");
        if (hecho.getLatitud() == null || hecho.getLongitud() == null) faltantes.add("Ubicación (lat/long)");
        if (hecho.getMunicipio() == null || hecho.getMunicipio().isBlank()) faltantes.add("Municipio");
        if (hecho.getProvincia() == null || hecho.getProvincia().isBlank()) faltantes.add("Provincia");
        if (hecho.getFechaAcontecimiento() == null) faltantes.add("Fecha de Acontecimiento");
        return faltantes;
    }
}
