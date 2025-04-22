package models.entities.filtros;

import lombok.Getter;
import models.entities.criterios.ElementoCriterio;

@Getter
public class Ubicacion {
    private String latitud;
    private String longitud;

    public Ubicacion(String latitud, String longitud) throws RuntimeException{
        if (Double.parseDouble(latitud) < -90 || Double.parseDouble(latitud) > 90){
            throw new RuntimeException("Latitud no valido");
        }

        if(Double.parseDouble(longitud) < -180 || Double.parseDouble(longitud) > 180){
            throw new RuntimeException("Longitud no valido");
        }
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Boolean coincide(Ubicacion ubicacion) throws RuntimeException {
        if(!(ubicacion instanceof Ubicacion)){
            throw new RuntimeException("Ubicacion Invalida");
        }
        return  this.latitud.equals(ubicacion.latitud) &&
                this.longitud.equals(ubicacion.longitud);
    }
}
