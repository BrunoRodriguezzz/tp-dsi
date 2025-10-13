package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;

import java.time.LocalDateTime;

public class Utils {
    public static QueryParamsFiltro crearFiltros(String categoria, LocalDateTime fechaAcontecimientoInicio, LocalDateTime fechaAcontecimientoFin, String titulo, String latitud, String longitud, LocalDateTime fechaCargaInicio, LocalDateTime fechaCargaFin) {
        QueryParamsFiltro params = new QueryParamsFiltro();
        params.setCategoria(categoria);
        params.setFechaAcontecimientoInicio(fechaAcontecimientoInicio);
        params.setFechaAcontecimientoFin(fechaAcontecimientoFin);
        params.setFechaCargaInicio(fechaCargaInicio);
        params.setFechaCargaFin(fechaCargaFin);
        params.setLatitud(latitud);
        params.setLongitud(longitud);
        params.setTitulo(titulo);
        return params;
    }
}
