package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;

public class FiltroCategoria implements Filtro {
    private Categoria categoria;

    public FiltroCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Boolean coincide(Hecho hecho) {
        return this.categoria.getTitulo().equals(hecho.getCategoria().getTitulo());
    }
}
