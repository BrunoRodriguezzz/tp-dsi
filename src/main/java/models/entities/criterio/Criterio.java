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
        Boolean resultado = this.filtros.stream().allMatch(filtro ->
            filtro.coincide(hecho)
        );
        return resultado;
    }

    public void agregarFiltro(Filtro filtro) {
        this.filtros.add(filtro);
    }
}