package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroCategoria;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaAcontecimientoFinal;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaAcontecimientoInicio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaCargaFinal;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroFechaCargaInicio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroLatitud;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroLongitud;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl.FiltroTitulo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CriterioInputDTO {
    String titulo;
    LocalDate fechaAcontecimientoInicio;
    LocalDate fechaAcontecimientoFin;
    LocalDate fechaCargaInicio;
    LocalDate fechaCargaFin;
    String latitud;
    String longitud;
    String categoria;

    public static List<Filtro> crearFiltros(CriterioInputDTO criterioInputDTO) {
        List<Filtro> filtros = new ArrayList<>();

        try {
            if(criterioInputDTO == null) {
                return new ArrayList<>();
            }

            if (criterioInputDTO.getTitulo() != null) { // Quiero filtrar por título
                if(!criterioInputDTO.getTitulo().isEmpty()) {
                    FiltroTitulo filtroTitulo = new FiltroTitulo(criterioInputDTO.getTitulo());
                    filtros.add(filtroTitulo);
                }
            }

            if (criterioInputDTO.getFechaAcontecimientoInicio() != null) {
                FiltroFechaAcontecimientoInicio filtroFechaAcontecimientoInicio = new FiltroFechaAcontecimientoInicio();
                filtroFechaAcontecimientoInicio.setFechaInicio(criterioInputDTO.getFechaAcontecimientoInicio());
                filtros.add(filtroFechaAcontecimientoInicio);
            }

            if (criterioInputDTO.getFechaAcontecimientoFin() != null) {
                FiltroFechaAcontecimientoFinal filtroFechaAcontecimientoFinal = new FiltroFechaAcontecimientoFinal();
                filtroFechaAcontecimientoFinal.setFechaFinal(criterioInputDTO.getFechaAcontecimientoFin());
                filtros.add(filtroFechaAcontecimientoFinal);
            }

            if (criterioInputDTO.getFechaCargaInicio() != null) {
                FiltroFechaCargaInicio filtroFechaCargaInicio = new FiltroFechaCargaInicio();
                filtroFechaCargaInicio.setFechaInicio(criterioInputDTO.getFechaCargaInicio());
                filtros.add(filtroFechaCargaInicio);
            }

            if (criterioInputDTO.getFechaCargaFin() != null) {
                FiltroFechaCargaFinal filtroFechaCargaFinal = new FiltroFechaCargaFinal();
                filtroFechaCargaFinal.setFechaFinal(criterioInputDTO.getFechaCargaFin());
                filtros.add(filtroFechaCargaFinal);
            }

            if (criterioInputDTO.getLatitud() != null) {
                FiltroLatitud filtroLatitud = new FiltroLatitud(criterioInputDTO.getLatitud());
                filtros.add(filtroLatitud);
            }

            if (criterioInputDTO.getLongitud() != null) {
                FiltroLongitud filtroLongitud = new FiltroLongitud(criterioInputDTO.getLatitud());
                filtros.add(filtroLongitud);
            }

            if (criterioInputDTO.getCategoria() != null) { // Quiero filtrar por categoría
                if(!criterioInputDTO.getCategoria().isEmpty()) {
                    try {
                        FiltroCategoria filtroCategoria = new FiltroCategoria(new Categoria(criterioInputDTO.getCategoria()));
                        filtros.add(filtroCategoria);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        } catch (Exception e){ throw new RuntimeException(e); }

        return filtros;
    }
}
