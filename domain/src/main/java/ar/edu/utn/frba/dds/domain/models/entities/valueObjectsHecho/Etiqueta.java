package ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho;

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
