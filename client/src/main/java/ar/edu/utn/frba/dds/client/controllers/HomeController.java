package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.services.HechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private HechoService hechoService;

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");
        List<HechoDTO> hechosDestacados = this.hechoService.obtenerHechos().stream().limit(3).toList();
        model.addAttribute("hechosDestacados", hechosDestacados);
        return "landingPage/index";
    }

    @GetMapping("home/sobreNosotros")
    public String sobreNosotros(Model model){
        return "sobreNosotros";
    }
}