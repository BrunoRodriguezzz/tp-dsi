package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FiltroFechaCargaFinal implements Filtro {
  private LocalDateTime fechaFinal;

  public Boolean coincide(Hecho hecho) {
    return hecho.getFechaCarga().isBefore(this.fechaFinal);
  }

  public String toDTO() {
    return this.fechaFinal.toString();
  }
}
