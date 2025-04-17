package entities;

public class Ubicacion implements ElementoCriterio<Ubicacion> {
    private String latitud;
    private String longitud;

    @Override
    public Boolean coincide(Ubicacion ubicacion) throws RuntimeException {
        if(!(ubicacion instanceof Ubicacion)){
            throw new RuntimeException("Ubicacion Invalida");
        }
        return  this.latitud.equals(ubicacion.latitud) &&
                this.longitud.equals(ubicacion.longitud);
    }
}
