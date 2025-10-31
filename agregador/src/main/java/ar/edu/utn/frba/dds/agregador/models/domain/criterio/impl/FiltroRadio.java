package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Radio")
@NoArgsConstructor
@Getter
@Setter
public class FiltroRadio extends EntidadFiltro {
    private double latitud;
    private double longitud;
    private double radioKm;

    public FiltroRadio(double latitud, double longitud, double radioKm) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.radioKm = radioKm;
    }

    @Override
    public Boolean coincide(Hecho hecho) {
        if (hecho.getUbicacion() == null) return false;
        double distancia = haversineKm(latitud, longitud, hecho.getUbicacion().getLatitud(), hecho.getUbicacion().getLongitud());
        return distancia <= radioKm;
    }

    @Override
    public String toDTO() {
        return String.format("radio: %f,%f,%fkm", latitud, longitud, radioKm);
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}

