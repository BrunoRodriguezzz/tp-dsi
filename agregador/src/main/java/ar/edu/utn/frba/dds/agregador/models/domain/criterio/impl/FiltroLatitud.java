package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Latitud")
@NoArgsConstructor
@Getter
@Setter
public class FiltroLatitud extends EntidadFiltro {
  private double latitud;

  public FiltroLatitud(double latitud) {
    if (!esLatitudValida(latitud)) {
      throw new IllegalArgumentException("Latitud inválida: " + latitud);
    }
    this.latitud = latitud;
  }

  @Override
  public Boolean coincide(Hecho hecho) {
    if (hecho.getUbicacion() == null) return false;
    double epsilon = 0.0001; // tolerancia ~11m
    return Math.abs(hecho.getUbicacion().getLatitud() - latitud) <= epsilon;
  }

  @Override
  public String toDTO() {
    return "latitud: " + this.latitud;
  }

  private boolean esLatitudValida(double lat) {
      return lat >= -90 && lat <= 90;
  }
}
