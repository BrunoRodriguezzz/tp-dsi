package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.estadisticas.*;
import ar.edu.utn.frba.dds.client.services.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {
    @Autowired
    private EstadisticaService estadisticaService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public String getEstadisticasGenerales(Model model){
        model.addAttribute("titulo", "Estadísticas y Análisis");

        // Cards de Estadísticas Generales
        EstadisticaSolicitudesDTO cantSpam = this.estadisticaService.getCantSolicitudesSpam();
        model.addAttribute("cantSpam", cantSpam.getSolicitudes_spam());
        model.addAttribute("cantNoSpam", cantSpam.getSolicitudes_no_spam());
        EstadisticaCategoriaDTO categorias = this.estadisticaService.getRankingCategorias();
        model.addAttribute("categoriaTop", categorias.getCategoriasConMasHechos().entrySet().iterator().next().getKey());
        // model.addAttribute("totalHechos",categorias.getCategoriasConHechos().values().stream().reduce(0L,Long::sum));
        List<EstadisticaProvinciaXCategoriaDTO> categoriasPorProvincia = this.estadisticaService.getCategoriasPorProvincias();
        model.addAttribute("totalHechos", categoriasPorProvincia
                .stream()
                .map(EstadisticaProvinciaXCategoriaDTO::getProvinciasConHechos)
                .flatMap(map -> map.values().stream())
                .reduce(0L, Long::sum));

        // Tab Content de "Colección por Provincia"
        List<EstadisticaProvinciaXColeccionDTO> estadisticasProvinciasPorColecciones = this.estadisticaService.getRankingProvinciasPorColeccion().stream().limit(8).toList();
        model.addAttribute("totalColecciones",estadisticasProvinciasPorColecciones.size());
        model.addAttribute("colecciones", estadisticasProvinciasPorColecciones.stream().map(EstadisticaProvinciaXColeccionDTO::getColeccion).toList());
        model.addAttribute("coleccionPorProvincia", this.estadisticaService.convertToMap(estadisticasProvinciasPorColecciones));
        model.addAttribute("resultProvincia",estadisticasProvinciasPorColecciones.get(0).getProvinciasConHechos().entrySet().iterator().next().getKey());
        model.addAttribute("resultCantidad",estadisticasProvinciasPorColecciones.get(0).getProvinciasConHechos().values().iterator().next());

        // Tab Content "Ranking de Categorías"
        model.addAttribute("rankingCategorias", categorias.getCategoriasConMasHechos());

        // Tab Categorías Por Provincia
        model.addAttribute("categorias", categoriasPorProvincia.stream()
                .map(EstadisticaProvinciaXCategoriaDTO::getCategoria)
                .collect(Collectors.toList()));
        model.addAttribute("categoriasPorProvincia",categoriasPorProvincia);

        // Tab "Distribución Horaria de Hechos"
        List<EstadisticaHoraXCategoriaDTO> horariosPorCategoria = this.estadisticaService.getHorariosPorCategoria();
        model.addAttribute("horariosCategorias", horariosPorCategoria
                .stream()
                .map(EstadisticaHoraXCategoriaDTO::getCategoria)
                .toList());
        model.addAttribute("horariosPorCategoria",horariosPorCategoria);
        return "estadisticasAnalisis/estadisticas";
    }
}

