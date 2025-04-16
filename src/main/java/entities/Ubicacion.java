package entities;

public class Ubicacion implements ElementoCriterio<Ubicacion> {
    private String latitud;
    private String longitud;

    @Override
    public Boolean coincide(Ubicacion ubicacion) throws Exception {
        if(!(ubicacion instanceof Ubicacion)){
            throw new Exception("Ubicacion Invalida");
        }
        return  this.latitud.equals(ubicacion.latitud) &&
                this.longitud.equals(ubicacion.longitud);
    }
}
