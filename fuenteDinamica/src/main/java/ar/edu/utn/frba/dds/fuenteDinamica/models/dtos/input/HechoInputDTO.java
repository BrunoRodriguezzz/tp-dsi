package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.ContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Etiqueta;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class HechoInputDTO {

    private String        nombreUsuario;
    private String        apellidoUsuario;
    private LocalDate     fechaNacimientoUsuario;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;

    private String        latitud;
    private String        longitud;
    private String        provincia;
    private String        municipio;

    private LocalDateTime fechaAcontecimiento;
    private Long          idUsuario;

    public static HechoInputDTO convertirModAInput(HechoModificadoInputDTO hecho){
        return HechoInputDTO
                .builder()
                .titulo(hecho.getTitulo())
                .nombreUsuario(hecho.getNombreUsuario())
                .apellidoUsuario(hecho.getApellidoUsuario())
                .fechaNacimientoUsuario(hecho.getFechaNacimientoUsuario())
                .descripcion(hecho.getDescripcion())
                .categoria(hecho.getCategoria())
                .contenidoMultimedia(hecho.getContenidoMultimedia())
                .latitud(hecho.getLatitud())
                .longitud(hecho.getLongitud())
                .fechaAcontecimiento(hecho.getFechaAcontecimiento())
                .idUsuario(hecho.getIdUsuario())
                .build();
    }
}