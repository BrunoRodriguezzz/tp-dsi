package models.entities.valueObjectsHecho;

import lombok.Getter;

@Getter
public class Ubicacion {
    private String latitud;
    private String longitud;

    public Ubicacion(String latitud, String longitud) throws RuntimeException{
        if(this.latitudInvalida(latitud)){
            throw new RuntimeException("Latitud invalida: " + latitud);
        }

        if(this.longitudInvalida(longitud)){
            throw new RuntimeException("Longitud invalida: " + longitud);
        }

        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Boolean coincide(Ubicacion ubicacion) throws RuntimeException {
        if(this.latitudInvalida(ubicacion.latitud) || this.longitudInvalida(ubicacion.longitud)){
            throw new RuntimeException("Valores de Ubicacion Invalidos");
        }
        return  this.latitud.equals(ubicacion.latitud) &&
                this.longitud.equals(ubicacion.longitud);
    }

    public boolean latitudInvalida(String latitud) {
        return (Double.parseDouble(latitud) < -90) || (Double.parseDouble(latitud) > 90);
    }

    public boolean longitudInvalida(String longitud){
        return (Double.parseDouble(longitud) < -180) || (Double.parseDouble(longitud) > 180);
    }


}
