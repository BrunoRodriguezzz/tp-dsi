package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class HechoOutputDTO {
    private Long           id;
    private Contribuyente  contribuyente;
    private String         titulo;
    private String         descripcion;
    private String         categoria;
    private List<String>   contenidoMultimedia;
    private Ubicacion      ubicacion;
    private LocalDate      fechaAcontecimiento;
    private List<String>   etiquetas;
    private String         origen;
}