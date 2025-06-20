package ar.edu.utn.frba.dds.domain.models.entities.fuentes;

import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;
import lombok.Getter;
import lombok.Setter;

public class FuenteDinamica {
    private Contribuyente contribuyente;

    public FuenteDinamica(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }
}
