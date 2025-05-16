package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class HechoInputDTO {

    private Contribuyente contribuyente;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private String        urlMultimedia;
    private String        ubicacion;
    private LocalDate     fechaAcontecimiento;
}