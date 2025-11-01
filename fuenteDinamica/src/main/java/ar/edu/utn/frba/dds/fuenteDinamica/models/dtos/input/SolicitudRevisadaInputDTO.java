package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SolicitudRevisadaInputDTO {

    private Long id;
    private Long idHecho;
    private EstadoHecho estadoHecho;

}
