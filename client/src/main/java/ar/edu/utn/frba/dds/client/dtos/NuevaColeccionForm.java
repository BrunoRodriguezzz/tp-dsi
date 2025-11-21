package ar.edu.utn.frba.dds.client.dtos;

import lombok.Data;

import java.util.List;

@Data
public class NuevaColeccionForm {
    private String nombre;
    private String descripcion;
    private List<String> fuentes;
    private List<String> consensos;

    // Campos del criterio
    private String criterioCategoria;
    private String criterioTitulo;
    private String criterioLatitud;
    private String criterioLongitud;
    private String criterioFechaCargaInicio;
    private String criterioFechaCargaFin;
    private String criterioFechaAcontecimientoInicio;
    private String criterioFechaAcontecimientoFin;
}

