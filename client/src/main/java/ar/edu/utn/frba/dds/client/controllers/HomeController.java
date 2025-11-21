package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.services.ColeccionService;
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
    @Autowired
    private ColeccionService coleccionService;

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");
        List<HechoDTO> hechosDestacados = this.hechoService.obtenerHechos();
        List<ColeccionOutputDTO> coleccionesDestacadas = this.coleccionService.obtenerColecciones().stream().limit(9).toList();
        model.addAttribute("hechosDestacados", hechosDestacados);
        model.addAttribute("coleccionesDestacadas", coleccionesDestacadas);
        return "landingPage/index";
    }

    @GetMapping("home/sobreNosotros")
    public String sobreNosotros(Model model){
        model.addAttribute("titulo", "Sistema de Mapeo Colaborativo");
        return "sobreNosotros";
    }

    @GetMapping("/privacidad")
    public String privacidad(Model model){
        return "privacidad";
    }
}