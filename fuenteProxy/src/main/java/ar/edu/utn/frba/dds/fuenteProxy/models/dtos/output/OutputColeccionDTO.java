package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OutputColeccionDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String criterio;
    private List<Fuente> fuentes;
    private List<HechoProxy> hechos;
}
