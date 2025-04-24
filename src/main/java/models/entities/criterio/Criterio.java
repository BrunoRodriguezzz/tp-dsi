package models.entities.criterio;

import lombok.Setter;
import models.entities.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

@Setter
public class Criterio {
    private List<Filtro> filtros;

    public Criterio() {
        this.filtros = new ArrayList<>();
    }

    public Boolean cumpleCriterio(Hecho hecho) {
        Boolean resultado = false;
        resultado = this.filtros.stream().allMatch(filtro ->
                {
                    try {
                        return filtro.coincide(hecho);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            );
        return resultado;
    }

    public void agregarFiltro(Filtro filtro) {
        this.filtros.add(filtro);
    }
}