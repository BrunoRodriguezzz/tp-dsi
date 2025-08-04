package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDate;
import lombok.Data;

@Data
public class FiltroFechaAcontecimientoFinal implements Filtro {
  private LocalDate fechaFinal;

  public Boolean coincide(Hecho hecho) {
    return hecho.getFechaAcontecimiento().isBefore(this.fechaFinal);
  }

  public String toDTO() {
    return this.fechaFinal.toString();
  }
}
