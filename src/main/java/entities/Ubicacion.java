package entities;

import lombok.Getter;

@Getter
public class Ubicacion implements ElementoCriterio<Ubicacion> {
    private String latitud;
    private String longitud;

    public Ubicacion(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public Boolean coincide(Ubicacion ubicacion) throws RuntimeException {
        if(!(ubicacion instanceof Ubicacion)){
            throw new RuntimeException("Ubicacion Invalida");
        }
        return  this.latitud.equals(ubicacion.latitud) &&
                this.longitud.equals(ubicacion.longitud);
    }
}
