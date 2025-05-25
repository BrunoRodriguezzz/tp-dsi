package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class HechoInputDTO {

    private String        nombreUsuario;
    private LocalDate     fechaNacimientoUsuario;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private Double        latitud;
    private Double        longitud;
    private LocalDate     fechaAcontecimiento;

}