package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Etiqueta")
@NoArgsConstructor
@Getter
@Setter
public class FiltroEtiqueta extends EntidadFiltro {
  private String etiqueta;

  public FiltroEtiqueta(String etiqueta) {
    this.etiqueta = etiqueta;
  }

  @Override
  public Boolean coincide(Hecho hecho) {
    return hecho.getEtiquetas() != null && hecho.getEtiquetas().contains(this.etiqueta);
  }

  @Override
  public String toDTO() {
    return this.etiqueta;
  }
}
