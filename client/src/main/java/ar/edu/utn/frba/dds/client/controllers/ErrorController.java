package ar.edu.utn.frba.dds.client.controllers;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/403")
public class ErrorController {
@GetMapping
    public String mostrarFormulario(Model model) {
        return "e403";
    }
}
