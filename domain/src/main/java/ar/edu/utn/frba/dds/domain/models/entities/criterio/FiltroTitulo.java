package ar.edu.utn.frba.dds.domain.models.entities.criterio;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;

public class FiltroTitulo implements Filtro {
    private String titulo;

    public FiltroTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean coincide(Hecho hecho) {
        return this.titulo.equals(hecho.getTitulo());
    }
}
