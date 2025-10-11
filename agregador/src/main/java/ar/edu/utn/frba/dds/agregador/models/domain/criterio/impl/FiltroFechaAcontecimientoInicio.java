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
@DiscriminatorValue("FechaAcontecimientoInicio")
@NoArgsConstructor
@Getter
@Setter
public class FiltroFechaAcontecimientoInicio extends EntidadFiltro {
  private LocalDateTime fechaInicio;

  public FiltroFechaAcontecimientoInicio(LocalDateTime fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public Boolean coincide(Hecho hecho) {
    if (hecho.getFechaCarga() == null || fechaInicio == null) return false;
    return !hecho.getFechaCarga().isBefore(fechaInicio);
  }

  public String toDTO() {
    return fechaInicio != null ? fechaInicio.toString() : null;
  }
}
