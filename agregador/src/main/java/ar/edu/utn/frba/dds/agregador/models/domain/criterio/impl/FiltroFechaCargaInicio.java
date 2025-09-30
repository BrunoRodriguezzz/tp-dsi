package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FiltroFechaCargaInicio implements Filtro {
  private LocalDateTime fechaInicio;

  public Boolean coincide(Hecho hecho) {
    return hecho.getFechaCarga().isAfter(this.fechaInicio);
  }

  public String toDTO() {
    return this.fechaInicio.toString();
  }
}
