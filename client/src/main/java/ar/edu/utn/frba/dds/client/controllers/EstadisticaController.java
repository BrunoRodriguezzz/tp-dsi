package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.estadisticas.*;
import ar.edu.utn.frba.dds.client.services.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {
    @Autowired
    private EstadisticaService estadisticaService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public String getEstadisticasGenerales(Model model){
        model.addAttribute("titulo", "Estadisticas y Análisis");

        // Cards de Estadísticas Generales
        EstadisticaCategoriaDTO categorias = this.estadisticaService.getCategorias();
        model.addAttribute("categoriaTop", categorias.getCategoriasConHechos().entrySet().iterator().next().getKey());
        EstadisticaSolicitudesDTO cantSpam = this.estadisticaService.getCantSolicitudesSpam();
        model.addAttribute("cantSpam", cantSpam.getSolicitudes_spam());
        model.addAttribute("cantNoSpam", cantSpam.getSolicitudes_no_spam());
        model.addAttribute("totalHechos",categorias.getCategoriasConHechos().values().stream().reduce(0L,Long::sum));

        // Tab Content de "Colección por Provincia"
        List<EstadisticaProvinciaXColeccionDTO> estadisticasProvinciasPorColeccion = this.estadisticaService.getRankingProvinciasPorColeccion().stream().limit(8).toList();
        model.addAttribute("totalColecciones",estadisticasProvinciasPorColeccion.size());
        model.addAttribute("colecciones", estadisticasProvinciasPorColeccion.stream().map(EstadisticaProvinciaXColeccionDTO::getColeccion).distinct().toList());

        System.out.println("Provincias por Colección : " + estadisticasProvinciasPorColeccion);
        Map<String, Map<String, Long>> coleccionPorProvincia =
                estadisticasProvinciasPorColeccion.stream()
                        .collect(Collectors.toMap(
                                EstadisticaProvinciaXColeccionDTO::getColeccion,
                                EstadisticaProvinciaXColeccionDTO::getProvinciasConHechos,
                                (existing, replacement) -> existing,  // merge function (en caso de duplicados)
                                LinkedHashMap::new
                        ));
        System.out.println("coleccionPorProvincia = " + coleccionPorProvincia);


        model.addAttribute("coleccionPorProvincia", coleccionPorProvincia);
        model.addAttribute("resultProvincia",estadisticasProvinciasPorColeccion.get(0).getProvinciasConHechos().entrySet().iterator().next().getKey());
        model.addAttribute("resultCantidad",estadisticasProvinciasPorColeccion.get(0).getProvinciasConHechos().values().iterator().next());

        // Tab Content "Ranking de Categorías"
        model.addAttribute("rankingCategorias",
                coleccionPorProvincia
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, // la categoría
                                e -> e.getValue().values().stream().mapToLong(Long::longValue).sum(), // suma de sus provincias
                                (existing, replacement) -> existing,  // merge function (en caso de duplicados)
                                LinkedHashMap::new
                        )));

        Map<String, Long> rankingCategorias =
                coleccionPorProvincia.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().values().stream().mapToLong(Long::longValue).sum(),
                                (existing, replacement) -> existing,  // merge function (en caso de duplicados)
                                LinkedHashMap::new
                        ));

        rankingCategorias.forEach((categoria, total) ->
                System.out.println(categoria + " = " + total)
        );

        // Tab Categorías Por Provincia
        List<EstadisticaProvinciaXCategoriaDTO> categoriasPorProvincia = this.estadisticaService.getCategoriasPorProvincias();
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

