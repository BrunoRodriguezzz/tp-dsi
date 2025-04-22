package models.entities.filtros;

import models.entities.criterios.Filtro;
import models.entities.hechos.Hecho;

public class FiltroCategoria implements Filtro {
    private Categoria categoria;

    public FiltroCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Boolean coincide(Hecho hecho) {
        return this.categoria.coincide(hecho.getCategoria());
    }
}
