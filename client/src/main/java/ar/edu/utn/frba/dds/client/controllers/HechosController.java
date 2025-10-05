package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.HechoDTO;
import ar.edu.utn.frba.dds.client.services.HechoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechosController {
    private final HechoService hechoService;

    @GetMapping("/hechos")
    public String listarHechos(Model model) {
        List<HechoDTO> hechos = this.hechoService.obtenerHechos();
        model.addAttribute("hechos", hechos);
        model.addAttribute("cantidad", hechos.size());
        return "hechos";
    }
}
