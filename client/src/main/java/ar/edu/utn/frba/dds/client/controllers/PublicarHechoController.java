package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.services.DinamicaService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/publicarHecho")
@RequiredArgsConstructor
public class PublicarHechoController {

    private final DinamicaService dinamicaService;
    private final Logger LOGGER = LogManager.getLogger(HechoController.class);

    @GetMapping
    public String publicacionHecho(Model model){
        model.addAttribute("titulo", "Publicar Hecho");
        return "publicarHecho";
    }

    @PostMapping
    public String publicarHecho(
            @RequestParam(required = false) Long idContribuyente,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String categoria,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaAcontecimiento,
            @RequestParam String municipio,
            @RequestParam String provincia,
            @RequestParam Double latitud,
            @RequestParam Double longitud) {

        LOGGER.info("Id del usuario: {}", idContribuyente);
        LOGGER.info("Titulo: {}", titulo);
        LOGGER.info("Descripcion del hecho: {}", descripcion);
        LOGGER.info("Categoria: {}", categoria);
        LOGGER.info("Fecha del acontecimiento: {}", fechaAcontecimiento);
        LOGGER.info("Municipio: {}", municipio);
        LOGGER.info("Provincia: {}", provincia);
        LOGGER.info("Latitud: {}", latitud);
        LOGGER.info("Longitud: {}", longitud);

        return "redirect:/hechos";
    }
}
