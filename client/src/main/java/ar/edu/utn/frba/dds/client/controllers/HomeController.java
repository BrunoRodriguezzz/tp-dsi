package ar.edu.utn.frba.dds.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");

        return "landingPage/index";
    }
}
