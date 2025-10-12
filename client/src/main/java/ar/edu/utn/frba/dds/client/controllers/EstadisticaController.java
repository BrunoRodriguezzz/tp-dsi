package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.EstadisticaCategoriaDTO;
import ar.edu.utn.frba.dds.client.dtos.EstadisticaProvinciaXColeccionDTO;
import ar.edu.utn.frba.dds.client.dtos.EstadisticaSolicitudesDTO;
import ar.edu.utn.frba.dds.client.services.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {
    @Autowired
    private EstadisticaService estadisticaService;

    @GetMapping
    public String getEstadisticasGenerales(Model model){
        model.addAttribute("titulo", "Estadisticas y Análisis");

        // Cards de Estadísticas Generales
        EstadisticaCategoriaDTO categoriaTop = this.estadisticaService.getCategoriaTop();
        model.addAttribute("categoriaTop", categoriaTop.getCategoriasConHechos().entrySet().iterator().next().getKey());
        EstadisticaSolicitudesDTO cantSpam = this.estadisticaService.getCantSolicitudesSpam();
        model.addAttribute("cantSpam", cantSpam.getSolicitudes_spam());
        model.addAttribute("totalHechos",categoriaTop.getCategoriasConHechos().values().stream().reduce(0L,Long::sum));
        // TODO: me falta este
        model.addAttribute("totalColecciones",89);

        // Tab Content de "Colección por Provincia"
        List<EstadisticaProvinciaXColeccionDTO> estadisticasProvinciasPorColeccion = this.estadisticaService.getRankingProvinciasPorColeccion().stream().limit(8).toList();
        model.addAttribute("colecciones", estadisticasProvinciasPorColeccion.stream().map(EstadisticaProvinciaXColeccionDTO::getColeccion).distinct().toList());
        model.addAttribute("resultProvincia",estadisticasProvinciasPorColeccion.get(0).getProvinciasConHechos().entrySet().iterator().next().getKey());
        model.addAttribute("resultCantidad",estadisticasProvinciasPorColeccion.get(0).getProvinciasConHechos().values().iterator().next());
        return "estadisticasAnalisis/estadisticas";
    }
}

