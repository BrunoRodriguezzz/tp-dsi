package ar.edu.utn.frba.dds.domain.models.entities.criterio;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import models.entities.hechos.Hecho;

public class FiltroCategoria implements Filtro {
    private Categoria categoria;

    public FiltroCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Boolean coincide(Hecho hecho) {
        return this.categoria.getTitulo().equals(hecho.getCategoria().getTitulo());
    }
}
