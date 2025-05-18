package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class HechoModificadoInputDTO {

    private Long          id;
    private String        nombreUsuario;
    private Integer       edadUsuario;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private String        urlMultimedia;
    private String        ubicacion;
    private LocalDate     fechaAcontecimiento;
}