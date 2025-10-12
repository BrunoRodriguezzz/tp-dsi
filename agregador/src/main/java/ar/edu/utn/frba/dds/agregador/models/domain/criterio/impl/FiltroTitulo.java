package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Titulo")
@NoArgsConstructor
@Getter
@Setter
public class FiltroTitulo extends EntidadFiltro {
    private String titulo;

    public FiltroTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Boolean coincide(Hecho hecho) {
        return this.titulo != null && this.titulo.equals(hecho.getTitulo());
    }

    @Override
    public String toDTO() {
        return this.titulo;
    }
}
