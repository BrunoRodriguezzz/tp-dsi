package ar.edu.utn.frba.dds.agregador.models.domain.criterio;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;

//<TipoElementoCriterio>: Funciona con cualquier tipo de dato y se implementa en las que usan la interface
public interface Filtro<TipoElementoCriterio> {
    public Boolean coincide(Hecho hecho) throws Exception;
}
