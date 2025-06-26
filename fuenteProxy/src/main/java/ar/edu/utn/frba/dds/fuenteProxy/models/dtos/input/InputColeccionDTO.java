package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InputColeccionDTO {
    private String id;
    private String nombre;
    private String descripcion;
    private List<Long> idsHechos;
}
