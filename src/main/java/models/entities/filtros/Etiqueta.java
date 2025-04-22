package models.entities.filtros;

import lombok.Getter;
import models.entities.criterios.ElementoCriterio;
@Getter
public class Etiqueta {
    private String titulo;

    public Etiqueta(String titulo) {
        this.titulo = titulo;
    }
}
