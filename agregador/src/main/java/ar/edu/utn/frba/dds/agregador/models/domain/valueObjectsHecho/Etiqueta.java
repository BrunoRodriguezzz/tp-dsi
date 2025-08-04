package ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Etiqueta {
    @Setter
    private String titulo;

    public Etiqueta(String titulo) {
        this.titulo = titulo;
    }
}
