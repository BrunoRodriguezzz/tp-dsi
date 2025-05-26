package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class HechoModificadoInputDTO {

    private Long          idHecho;
    private String        nombreUsuario;
    private LocalDate     fechaNacimientoUsuario;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private String        latitud;
    private String        longitud;
    private LocalDate     fechaAcontecimiento;
}