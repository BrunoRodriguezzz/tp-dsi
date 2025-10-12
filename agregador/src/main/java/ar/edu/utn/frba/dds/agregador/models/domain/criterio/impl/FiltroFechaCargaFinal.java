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
@DiscriminatorValue("FechaCargaFinal")
@NoArgsConstructor
@Getter
@Setter
public class FiltroFechaCargaFinal extends EntidadFiltro {
  private LocalDateTime fechaFinal;

  public FiltroFechaCargaFinal(LocalDateTime fechaFinal) {
    this.fechaFinal = fechaFinal;
  }

  @Override
  public Boolean coincide(Hecho hecho) {
    if (hecho.getFechaCarga() == null || fechaFinal == null) return false;
    return !hecho.getFechaCarga().isBefore(fechaFinal);
  }

  @Override
  public String toDTO() {
    return fechaFinal != null ? fechaFinal.toString() : null;
  }
}
