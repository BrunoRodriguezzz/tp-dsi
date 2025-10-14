package ar.edu.utn.frba.dds.client.dtos;

import lombok.Data;

import java.time.LocalDateTime;

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
}
