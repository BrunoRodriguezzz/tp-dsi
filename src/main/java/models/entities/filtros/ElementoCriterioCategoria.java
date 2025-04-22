package models.entities.filtros;

import models.entities.criterios.ElementoCriterio;
import models.entities.hechos.Hecho;

public class ElementoCriterioCategoria implements ElementoCriterio {
    private Categoria categoria;

    public ElementoCriterioCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Boolean coincide(Hecho hecho) {
        return this.categoria.coincide(hecho.getCategoria());
    }
}
