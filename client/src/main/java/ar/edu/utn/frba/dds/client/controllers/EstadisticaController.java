package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.estadisticas.*;
import ar.edu.utn.frba.dds.client.services.EstadisticaService;
import ar.edu.utn.frba.dds.client.services.HechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {
    @Autowired
    private EstadisticaService estadisticaService;

    @Autowired
    private HechoService hechoService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public String getEstadisticasGenerales(Model model){
        model.addAttribute("titulo", "Estadísticas y Análisis");

        // ========== VALIDACIÓN: Solicitudes Spam/No Spam ==========
        EstadisticaSolicitudesDTO cantSpam = this.estadisticaService.getCantSolicitudesSpam();
        Long solicitudesSpam = (cantSpam != null && cantSpam.getSolicitudes_spam() != null)
                ? cantSpam.getSolicitudes_spam()
                : 0L;
        Long solicitudesNoSpam = (cantSpam != null && cantSpam.getSolicitudes_no_spam() != null)
                ? cantSpam.getSolicitudes_no_spam()
                : 0L;

        model.addAttribute("cantSpam", solicitudesSpam);
        model.addAttribute("cantNoSpam", solicitudesNoSpam);
        model.addAttribute("haySolicitudes", solicitudesSpam > 0 || solicitudesNoSpam > 0);

        // ========== VALIDACIÓN: Categorías ==========
        EstadisticaCategoriaDTO categorias = this.estadisticaService.getRankingCategorias();
        boolean hayCategorias = categorias != null
                && categorias.getCategoriasConMasHechos() != null
                && !categorias.getCategoriasConMasHechos().isEmpty();

        String categoriaTop = hayCategorias
                ? categorias.getCategoriasConMasHechos().entrySet().iterator().next().getKey()
                : "N/A";

        model.addAttribute("categoriaTop", categoriaTop);
        model.addAttribute("hayCategorias", hayCategorias);
        model.addAttribute("rankingCategorias",
                hayCategorias ? categorias.getCategoriasConMasHechos() : Collections.emptyMap());

        // ========== VALIDACIÓN: Categorías por Provincia ==========
        List<EstadisticaProvinciaXCategoriaDTO> categoriasPorProvincia =
                this.estadisticaService.getCategoriasPorProvincias();

        boolean hayCategoriasPorProvincia = categoriasPorProvincia != null
                && !categoriasPorProvincia.isEmpty();

        Long totalHechos = hayCategoriasPorProvincia
                ? categoriasPorProvincia.stream()
                .map(EstadisticaProvinciaXCategoriaDTO::getProvinciasConHechos)
                .flatMap(map -> map.values().stream())
                .reduce(0L, Long::sum)
                : 0L;

        model.addAttribute("totalHechos", totalHechos);
        model.addAttribute("hayCategoriasPorProvincia", hayCategoriasPorProvincia);
        model.addAttribute("categorias",
                hayCategoriasPorProvincia
                        ? categoriasPorProvincia.stream()
                        .map(EstadisticaProvinciaXCategoriaDTO::getCategoria)
                        .collect(Collectors.toList())
                        : Collections.emptyList());
        model.addAttribute("categoriasPorProvincia",
                hayCategoriasPorProvincia ? categoriasPorProvincia : Collections.emptyList());

        // ========== VALIDACIÓN: Colecciones por Provincia ==========
        List<EstadisticaProvinciaXColeccionDTO> estadisticasProvinciasPorColecciones =
                this.estadisticaService.getRankingProvinciasPorColeccion();

        boolean hayColecciones = estadisticasProvinciasPorColecciones != null
                && !estadisticasProvinciasPorColecciones.isEmpty();

        model.addAttribute("totalColecciones",
                hayColecciones ? estadisticasProvinciasPorColecciones.size() : 0);
        model.addAttribute("hayColecciones", hayColecciones);
        model.addAttribute("colecciones",
                hayColecciones
                        ? estadisticasProvinciasPorColecciones.stream()
                        .map(EstadisticaProvinciaXColeccionDTO::getColeccion)
                        .toList()
                        : Collections.emptyList());
        model.addAttribute("coleccionPorProvincia",
                hayColecciones
                        ? this.estadisticaService.convertToMap(estadisticasProvinciasPorColecciones)
                        : Collections.emptyMap());

        // Resultado de primera colección (si existe)
        if (hayColecciones) {
            Optional.of(estadisticasProvinciasPorColecciones)
                    .filter(lista -> !lista.isEmpty())
                    .map(lista -> lista.get(0))
                    .flatMap(primera -> primera.getProvinciasConHechos().entrySet().stream().findFirst())
                    .ifPresentOrElse(
                            entry -> {
                                model.addAttribute("resultProvincia", entry.getKey());
                                model.addAttribute("resultCantidad", entry.getValue());
                            },
                            () -> {
                                model.addAttribute("resultProvincia", "N/A");
                                model.addAttribute("resultCantidad", 0);
                            }
                    );
        } else {
            model.addAttribute("resultProvincia", "N/A");
            model.addAttribute("resultCantidad", 0);
        }

        // ========== VALIDACIÓN: Horarios por Categoría ==========
        List<EstadisticaHoraXCategoriaDTO> horariosPorCategoria =
                this.estadisticaService.getHorariosPorCategoria();

        boolean hayHorarios = horariosPorCategoria != null && !horariosPorCategoria.isEmpty();

        model.addAttribute("hayHorarios", hayHorarios);
        model.addAttribute("horariosCategorias",
                hayHorarios
                        ? horariosPorCategoria.stream()
                        .map(EstadisticaHoraXCategoriaDTO::getCategoria)
                        .toList()
                        : Collections.emptyList());
        model.addAttribute("horariosPorCategoria",
                hayHorarios ? horariosPorCategoria : Collections.emptyList());

        return "estadisticasAnalisis/estadisticas";
    }
}