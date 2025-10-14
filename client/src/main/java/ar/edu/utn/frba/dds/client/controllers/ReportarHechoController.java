package ar.edu.utn.frba.dds.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportarHecho")
public class ReportarHechoController {

    @GetMapping
    public String mostrarFormulario(Model model) {
        return "reportarHecho";
    }

    @PostMapping
    public String procesarFormulario() {
        return "redirect:/";
    }
}

