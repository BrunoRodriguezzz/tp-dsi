package ar.edu.utn.frba.dds.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class MapaController {

    @GetMapping("/mapa")
    public String mostrarMapa(Model model) {

        // Lista de puntos: nombre, latitud y longitud
        List<Map<String, Object>> puntos = List.of(
                Map.of("nombre", "Madrid", "lat", 40.4168, "lng", -3.7038),
                Map.of("nombre", "Barcelona", "lat", 41.3851, "lng", 2.1734),
                Map.of("nombre", "Valencia", "lat", 39.4699, "lng", -0.3763)
        );

        model.addAttribute("puntos", puntos);
        model.addAttribute("mapboxToken", "pk.eyJ1IjoiZmVybmFuZG8xN2EiLCJhIjoiY21nbnk3MDg2MXpteTJucHJsdDllNzZuZCJ9.KuFr7I-l2wBE6ONQk3GpGw"); // ← pon tu token aquí

        return "mapa";
    }
}