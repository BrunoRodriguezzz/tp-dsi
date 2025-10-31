package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Fuente")
@NoArgsConstructor
@Getter
@Setter
public class FiltroFuente extends EntidadFiltro {
    private String fuente;

    public FiltroFuente(String fuente) {
        this.fuente = fuente;
    }

    @Override
    public Boolean coincide(Hecho hecho) {
        if (hecho.getFuenteSet() == null || hecho.getFuenteSet().isEmpty() || fuente == null) return false;
        return hecho.getFuenteSet().stream().anyMatch(hf -> hf.getFuente() != null && hf.getFuente().getNombre() != null && hf.getFuente().getNombre().equalsIgnoreCase(fuente));
    }

    @Override
    public String toDTO() {
        return fuente;
    }
}

