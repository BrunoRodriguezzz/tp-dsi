package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Ubicacion {
    private Double latitud;
    private Double longitud;
}
