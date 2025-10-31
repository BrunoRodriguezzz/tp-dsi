package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("FechaAcontecimientoFinal")
@NoArgsConstructor
@Getter
@Setter
public class FiltroFechaAcontecimientoFinal extends EntidadFiltro {
  private LocalDateTime fechaFinal;

  public FiltroFechaAcontecimientoFinal(LocalDateTime fechaFinal) {
    this.fechaFinal = fechaFinal;
  }

  public Boolean coincide(Hecho hecho) {
    if (hecho.getFechaAcontecimiento() == null || fechaFinal == null) return false;
    return !hecho.getFechaAcontecimiento().isAfter(fechaFinal);
  }

  public String toDTO() {
    return fechaFinal != null ? fechaFinal.toString() : null;
  }
}
