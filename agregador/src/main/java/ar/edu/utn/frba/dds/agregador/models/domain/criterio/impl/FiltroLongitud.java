package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

public class FiltroLongitud implements Filtro {
  private String longitud;

  public Boolean coincide(Hecho hecho) {
    return hecho.getUbicacion().getLongitud().equals(longitud);
  }

  public String toDTO() {
    return this.longitud;
  }
}
