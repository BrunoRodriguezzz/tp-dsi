package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

public class FiltroLongitud implements Filtro {
  private String longitud;

  public FiltroLongitud(String longitud) {
    if (!esLongitudValida(longitud)) {
      throw new IllegalArgumentException("Longitud inválida: " + longitud);
    }
    this.longitud = longitud;
  }

  public Boolean coincide(Hecho hecho) {
    return hecho.getUbicacion().getLongitud().equals(longitud);
  }

  public String toDTO() {
    return this.longitud;
  }

  private boolean esLongitudValida(String longitudStr) {
    try {
      double lon = Double.parseDouble(longitudStr);
      return lon >= -180 && lon <= 180;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
