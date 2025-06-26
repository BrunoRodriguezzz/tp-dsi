package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coleccion {
    private String id;
    private String nombre;
    private String descripcion;
    private List<Long> idsHechos;
}
