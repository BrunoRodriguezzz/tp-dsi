package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.ContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Etiqueta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class HechoInputDTO {

    private String       nombreUsuario;
    private String       apellidoUsuario;
    private LocalDate    fechaNacimientoUsuario;
    private String       titulo;
    private String       descripcion;
    private String       categoria;
    private List<String> contenidoMultimedia;
    private String       latitud;
    private String       longitud;
    private LocalDate    fechaAcontecimiento;
}