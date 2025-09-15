package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ubicacion")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String latitud;
    @Column(nullable = false)
    private String longitud;

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


}
