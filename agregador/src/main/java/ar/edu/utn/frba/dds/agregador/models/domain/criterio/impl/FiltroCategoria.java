package ar.edu.utn.frba.dds.agregador.models.domain.criterio.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Categoria")
@NoArgsConstructor
@Getter
@Setter
public class FiltroCategoria extends EntidadFiltro {
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "categoria_id", nullable = true)
    private Categoria categoria;

    public FiltroCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @PrePersist
    @PreUpdate
    private void ensureCategoriaManaged() {
        if (this.categoria != null && this.categoria.getId() == null) {
            throw new IllegalStateException(
                    "La categoría referenciada en FiltroCategoria no existe en la base de datos. Debe crearse o buscarse antes de persistir el filtro."
            );
        }
    }

    @Override
    public Boolean coincide(Hecho hecho) {
        return this.categoria.getTitulo().equals(hecho.getCategoria().getTitulo());
    }

    @Override
    public String toDTO() {
        return categoria != null ? categoria.getTitulo() : null;
    }
}
