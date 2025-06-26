package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;

public class FiltroTitulo implements Filtro {
    private String titulo;

    public FiltroTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean coincide(Hecho hecho) {
        return this.titulo.equals(hecho.getTitulo());
    }
}
