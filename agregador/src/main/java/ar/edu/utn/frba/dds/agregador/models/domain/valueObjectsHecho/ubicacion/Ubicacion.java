package ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.ValidationException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.UbicacionInvalidaException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Ubicacion {
    @Column(nullable = false)
    private double latitud;

    @Column(nullable = false)
    private double longitud;

    @Enumerated(EnumType.STRING)
    private Pais Pais;

    @Enumerated(EnumType.STRING)
    private Provincia Provincia;

    @Column
    private String municipio;

    public Ubicacion(double latitud, double longitud) {
        if(this.latitudInvalida(latitud)){
            throw new ValidationException("Latitud invalida: " + latitud);
        }
        if(this.longitudInvalida(longitud)){
            throw new ValidationException("Longitud invalida: " + longitud);
        }
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Boolean coincide(Ubicacion ubicacion) {
        if(this.latitudInvalida(ubicacion.latitud) || this.longitudInvalida(ubicacion.longitud)){
            throw new ValidationException("Valores de Ubicacion Invalidos: " + ubicacion.latitud + ";" + ubicacion.longitud);
        }
        return  this.latitud == ubicacion.latitud &&
                this.longitud == ubicacion.longitud;
    }

    public boolean latitudInvalida(double latitud) {
        return latitud < -90 || latitud > 90;
    }

    public boolean longitudInvalida(double longitud){
        return longitud < -180 || longitud > 180;
    }

    public boolean faltanDatos() {
        return (this.getProvincia() == null) ||
                (this.getPais() == null) ||
                (this.getMunicipio() == null || this.getMunicipio().isEmpty());
    }
}
