package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Longitud")
@NoArgsConstructor
@Getter
@Setter
public class FiltroLongitud extends EntidadFiltro {
  private double longitud;

  public FiltroLongitud(double longitud) {
    if (!esLongitudValida(longitud)) {
      throw new IllegalArgumentException("Longitud inválida: " + longitud);
    }
    this.longitud = longitud;
  }

  public Boolean coincide(Hecho hecho) {
    if (hecho.getUbicacion() == null) return false;
    double epsilon = 0.0001; // tolerancia ~11m
    return Math.abs(hecho.getUbicacion().getLongitud() - longitud) <= epsilon;
  }

  public String toDTO() {
    return "longitud: " + this.longitud;
  }

  private boolean esLongitudValida(double lon) {
      return lon >= -180 && lon <= 180;
  }
}
