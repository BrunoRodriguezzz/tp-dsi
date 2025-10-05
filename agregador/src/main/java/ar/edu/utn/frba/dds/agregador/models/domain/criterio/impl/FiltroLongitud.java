package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

public class FiltroLongitud implements Filtro {
  private double longitud;

  public FiltroLongitud(double longitud) {
    if (!esLongitudValida(longitud)) {
      throw new IllegalArgumentException("Longitud inválida: " + longitud);
    }
    this.longitud = longitud;
  }

  public Boolean coincide(Hecho hecho) {
    return hecho.getUbicacion().getLongitud() == longitud;
  }

  public String toDTO() {
    return "longitud: " + this.longitud;
  }

  private boolean esLongitudValida(double lon) {
      return lon >= -180 && lon <= 180;
  }
}
