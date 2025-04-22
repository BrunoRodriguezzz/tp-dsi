package models.entities.filtros;

import models.entities.criterios.ElementoCriterio;
import models.entities.hechos.Hecho;

public class ElementoCriterioTitulo implements ElementoCriterio {
    private String titulo;

    public ElementoCriterioTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean coincide(Hecho hecho) {
        return this.titulo.equals(hecho.getTitulo());
    }
}
