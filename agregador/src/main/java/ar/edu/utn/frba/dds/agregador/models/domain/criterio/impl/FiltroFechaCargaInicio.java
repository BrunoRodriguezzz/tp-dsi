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
@DiscriminatorValue("FechaCargaInicio")
@NoArgsConstructor
@Getter
@Setter
public class FiltroFechaCargaInicio extends EntidadFiltro {
  private LocalDateTime fechaInicio;

  public FiltroFechaCargaInicio(LocalDateTime fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  @Override
  public Boolean coincide(Hecho hecho) {
    if (hecho.getFechaCarga() == null || fechaInicio == null) return false;
    return !hecho.getFechaCarga().isBefore(fechaInicio);
  }

  @Override
  public String toDTO() {
    return fechaInicio != null ? fechaInicio.toString() : null;
  }
}
