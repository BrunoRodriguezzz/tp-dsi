package models.entities.criterios;

import lombok.Setter;
import models.entities.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

@Setter
public class Criterio {
    private List<Filtro> elementosCriterios;

    public Criterio() {
        this.elementosCriterios = new ArrayList<>();
    }

    public Boolean cumpleCriterio(Hecho hecho) {
        return this.elementosCriterios.stream().allMatch(elemento -> elemento.coincide(hecho));
    }

    public void agregarElementoCriterio(Filtro criterio) {
        this.elementosCriterios.add(criterio);
    }
}