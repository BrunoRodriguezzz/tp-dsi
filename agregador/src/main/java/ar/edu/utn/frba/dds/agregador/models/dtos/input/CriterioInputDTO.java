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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CriterioInputDTO {
    String titulo;
    LocalDateTime fechaAcontecimientoInicio;
    LocalDateTime fechaAcontecimientoFin;
    LocalDateTime fechaCargaInicio;
    LocalDateTime fechaCargaFin;
    String latitud;
    String longitud;
    String categoria;

    public static List<Filtro> crearFiltros(CriterioInputDTO criterioInputDTO) {
        List<Filtro> filtros = new ArrayList<>();

        try {
            if(criterioInputDTO == null) {
                return new ArrayList<>();
            }

            if (criterioInputDTO.getTitulo() != null && !criterioInputDTO.getTitulo().trim().isEmpty()) {
                FiltroTitulo filtroTitulo = new FiltroTitulo(criterioInputDTO.getTitulo());
                filtros.add(filtroTitulo);
            }

            if (criterioInputDTO.getCategoria() != null && !criterioInputDTO.getCategoria().trim().isEmpty()) {
                Categoria categoria = new Categoria();
                categoria.setTitulo(criterioInputDTO.getCategoria());
                filtros.add(new FiltroCategoria(categoria));
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

            if (criterioInputDTO.getLatitud() != null && !criterioInputDTO.getLatitud().trim().isEmpty()) {
                FiltroLatitud filtroLatitud = new FiltroLatitud(Double.parseDouble(criterioInputDTO.getLatitud()));
                filtros.add(filtroLatitud);
            }

            if (criterioInputDTO.getLongitud() != null && !criterioInputDTO.getLongitud().trim().isEmpty()) {
                FiltroLongitud filtroLongitud = new FiltroLongitud(Double.parseDouble(criterioInputDTO.getLongitud()));
                filtros.add(filtroLongitud);
            }


        } catch (Exception e){ throw new RuntimeException(e); }

        return filtros;
    }
}
