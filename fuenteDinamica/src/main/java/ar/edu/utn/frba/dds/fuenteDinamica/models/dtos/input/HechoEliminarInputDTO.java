package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class HechoEliminarInputDTO {
    private Long                  id;
    private String                titulo;
    private String                descripcion;
    private String                categoria;
    private UbicacionInputDTO     ubicacion;
    private LocalDateTime         fechaAcontecimiento;
    private ContribuyenteInputDTO contribuyente;
    private String                fuente;
    private String                origen;
}
