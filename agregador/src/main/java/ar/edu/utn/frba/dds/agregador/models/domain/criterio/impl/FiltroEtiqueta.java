package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

public class FiltroEtiqueta implements Filtro {
  private String nombre;

  public Boolean coincide(Hecho hecho) {
    return hecho.getEtiquetas().stream().anyMatch(e -> e.getTitulo().equals(this.nombre));
  }

  public String toDTO() {
    return this.nombre;
  }
}
