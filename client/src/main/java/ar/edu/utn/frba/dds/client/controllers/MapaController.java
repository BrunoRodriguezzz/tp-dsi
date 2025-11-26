package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.FuenteDTO;
import ar.edu.utn.frba.dds.client.dtos.hecho.HechoDTO;
import ar.edu.utn.frba.dds.client.services.FuenteService;
import ar.edu.utn.frba.dds.client.services.HechoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class MapaController {
    private final HechoService hechoService;
    private final FuenteService fuenteService;

    public MapaController(HechoService hechoService, FuenteService fuenteService) {
        this.fuenteService = fuenteService;
        this.hechoService = hechoService;
    }

    @GetMapping("/mapa")
    public String mostrarMapa(
            Model model,
            @RequestParam(name = "fechaDesde", required = false) LocalDateTime fechaAcontecimientoInicio,
            @RequestParam(name = "fechaHasta", required = false) LocalDateTime fechaAcontecimientoFin,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "fuente", required = false) String fuente
    ) {
//        List<HechoDTO> hechos = hechoService.obtenerHechosAgregador().getContent();
        List<HechoDTO> hechos = hechoService.obtenerHechosMapa(fechaAcontecimientoInicio, fechaAcontecimientoFin, categoria, fuente);
        List<FuenteDTO> fuentes = this.fuenteService.obtenerFuentes();
        model.addAttribute("titulo", "Mapa");
        model.addAttribute("hechos", hechos);
        model.addAttribute("fuentes", fuentes);
        model.addAttribute("mapboxToken", "pk.eyJ1IjoiZmVybmFuZG8xN2EiLCJhIjoiY21nbnk3MDg2MXpteTJucHJsdDllNzZuZCJ9.KuFr7I-l2wBE6ONQk3GpGw");

        model.addAttribute("fechaAcontecimientoInicio", fechaAcontecimientoInicio);
        model.addAttribute("fechaAcontecimientoFin", fechaAcontecimientoFin);
        model.addAttribute("categoria", categoria);
        model.addAttribute("fuenteSeleccionada", fuente);
        return "mapa";
    }
}