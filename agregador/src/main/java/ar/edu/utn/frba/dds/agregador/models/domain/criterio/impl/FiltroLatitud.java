package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

public class FiltroLatitud implements Filtro {
  private String latitud;

  public Boolean coincide(Hecho hecho) {
    return hecho.getUbicacion().getLatitud().equals(latitud);
  }

  public String toDTO() {
    return this.latitud;
  }
}
