package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InputColeccionDTO {
    private String titulo;
    private String descripcion;
    private String criterio;
    private List<Long> idsFuentes;
    private List<Long> idsHechos;
}
