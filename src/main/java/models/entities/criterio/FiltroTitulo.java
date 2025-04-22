package models.entities.criterio;

import models.entities.hechos.Hecho;

public class FiltroTitulo implements Filtro {
    private String titulo;

    public FiltroTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean coincide(Hecho hecho) {
        Boolean resultado = this.titulo.equals(hecho.getTitulo());
                                                                                                                                                                                                                                                                                                                                                                                                                                                        return resultado;
    }
}
