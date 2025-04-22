package models.entities.criterios;

import models.entities.hechos.Hecho;

//<TipoElementoCriterio>: Funciona con cualquier tipo de dato y se implementa en las que usan la interface
public interface ElementoCriterio<TipoElementoCriterio> {
    public Boolean coincide(Hecho hecho) throws RuntimeException;
}
