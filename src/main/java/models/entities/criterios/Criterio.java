package models.entities.criterios;

import lombok.Setter;
import models.entities.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

@Setter
public class Criterio {
    private List<ElementoCriterio> elementosCriterios;

    public Criterio() {
        this.elementosCriterios = new ArrayList<>();
    }

    public Boolean cumpleCriterio(Hecho hecho) throws RuntimeException{
        if(!(hecho instanceof Hecho)) {  //no me convence
            throw new RuntimeException("Instancia de hecho no valida");
        }
        return this.elementosCriterios.stream().allMatch(elemento -> elemento.coincide(hecho));
    }

    public void agregarCriterio(ElementoCriterio criterio) {
        this.elementosCriterios.add(criterio);
    }
}