package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.ContenidoMultimediaDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.EtiquetaOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.ContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class HechoModificadoInputDTO {

    private Long          id;
    private String        nombreUsuario;
    private String        apellidoUsuario;
    private LocalDate     fechaNacimientoUsuario;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private String        latitud;
    private String        longitud;
    private LocalDateTime fechaAcontecimiento;
    private Long          idUsuario;
}