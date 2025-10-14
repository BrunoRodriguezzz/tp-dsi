package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.services.DinamicaService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/modificarHecho")
@RequiredArgsConstructor
public class ModificarHechoController {

    private final DinamicaService dinamicaService;
    private final Logger LOGGER = LogManager.getLogger(HechoController.class);

    @PreAuthorize("hasAnyRole('ADMINISTRADOR') or hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/{id}")
    public String modificacionHecho(@PathVariable Long id, Model model){
        HechoDTO hecho = dinamicaService.buscarHechoId(id);
        model.addAttribute("hecho", hecho);
        model.addAttribute("titulo", "Modificacion");
        return "modificarHecho";
    }
}
