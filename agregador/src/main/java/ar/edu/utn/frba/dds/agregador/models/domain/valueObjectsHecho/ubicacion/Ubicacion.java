package ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion;

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
    private String latitud;

    @Column(nullable = false)
    private String longitud;

    @Enumerated(EnumType.STRING)
    private Pais Pais;

    @Enumerated(EnumType.STRING)
    private Provincia Provincia;

    @Column
    private String municipio;

    public Ubicacion(String latitud, String longitud) throws UbicacionInvalidaException {
        if(this.latitudInvalida(latitud)){
            throw new UbicacionInvalidaException("Latitud invalida: " + latitud);
        }
            if(this.longitudInvalida(longitud)){
            throw new UbicacionInvalidaException("Longitud invalida: " + longitud);
        }
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Boolean coincide(Ubicacion ubicacion) throws UbicacionInvalidaException {
        if(this.latitudInvalida(ubicacion.latitud) || this.longitudInvalida(ubicacion.longitud)){
            throw new UbicacionInvalidaException("Valores de Ubicacion Invalidos: " + ubicacion.latitud + ";" + ubicacion.longitud);
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

    public boolean faltanDatos() {
        return (this.getProvincia() == null) ||
                (this.getPais() == null) ||
                (this.getMunicipio() == null || this.getMunicipio().isEmpty());
    }
}
