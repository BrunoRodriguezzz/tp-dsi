package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

public class FiltroLatitud implements Filtro {
  private String latitud;

  public FiltroLatitud(String latitud) {
    if (!esLatitudValida(latitud)) {
      throw new IllegalArgumentException("Latitud inválida: " + latitud);
    }
    this.latitud = latitud;
  }

  public Boolean coincide(Hecho hecho) {
    return hecho.getUbicacion().getLatitud().equals(latitud);
  }

  public String toDTO() {
    return "latitud: " + this.latitud;
  }

  private boolean esLatitudValida(String latitudStr) {
    try {
      double lat = Double.parseDouble(latitudStr);
      return lat >= -90 && lat <= 90;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
