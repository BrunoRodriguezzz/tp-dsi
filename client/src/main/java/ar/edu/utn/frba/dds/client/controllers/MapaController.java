package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.services.HechoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class MapaController {
    private final HechoService hechoService;
    @Autowired
    public MapaController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/mapa")
    public String mostrarMapa(Model model) {
//        List<HechoDTO> hechos = hechoService.obtenerHechosAgregador().getContent();
        List<HechoDTO> hechos = hechoService.obtenerHechosMapa();
        model.addAttribute("titulo", "Mapa");
        model.addAttribute("hechos", hechos);
        model.addAttribute("mapboxToken", "pk.eyJ1IjoiZmVybmFuZG8xN2EiLCJhIjoiY21nbnk3MDg2MXpteTJucHJsdDllNzZuZCJ9.KuFr7I-l2wBE6ONQk3GpGw");
        return "mapa";
    }
}