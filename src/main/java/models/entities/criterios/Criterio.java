package models.entities.criterios;

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
        return this.filtros.stream().allMatch(elemento -> elemento.coincide(hecho));
    }

    public void agregarFiltro(Filtro filtro) {
        this.filtros.add(filtro);
    }
}