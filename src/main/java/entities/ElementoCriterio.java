package entities;
//<TipoElementoCriterio>: Funciona con cualquier tipo de dato y se implementa en las que usan la interface
public interface ElementoCriterio<TipoElementoCriterio> {
    public Boolean coincide(TipoElementoCriterio elementoCriterio) throws RuntimeException;
}
