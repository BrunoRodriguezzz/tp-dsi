package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.client.dtos.UbicacionDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public String publicarHecho(@ModelAttribute HechoInputDTO hecho) {

        //TODO: Necesario por ahora

        List<String> mult = new ArrayList<>();

        hecho.setContenidoMultimedia(mult);

        //

        this.dinamicaService.enviarHecho(hecho);

        return "redirect:/hechos";
    }
}
