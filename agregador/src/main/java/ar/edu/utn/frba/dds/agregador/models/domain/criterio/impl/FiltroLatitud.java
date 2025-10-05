package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

public class FiltroLatitud implements Filtro {
  private double latitud;

  public FiltroLatitud(double latitud) {
    if (!esLatitudValida(latitud)) {
      throw new IllegalArgumentException("Latitud inválida: " + latitud);
    }
    this.latitud = latitud;
  }

  public Boolean coincide(Hecho hecho) {
    return hecho.getUbicacion().getLatitud() == latitud;
  }

  public String toDTO() {
    return "latitud: " + this.latitud;
  }

  private boolean esLatitudValida(double lat) {
      return lat >= -90 && lat <= 90;
  }
}
