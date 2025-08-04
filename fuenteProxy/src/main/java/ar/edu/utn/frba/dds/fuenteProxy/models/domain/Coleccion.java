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
    private Long id;
    private String titulo;
    private String descripcion;
    private String criterio;
    private List<Fuente> fuentes;
    private List<Long> idsHechos;
}
