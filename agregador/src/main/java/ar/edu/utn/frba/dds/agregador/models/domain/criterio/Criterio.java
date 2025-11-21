package ar.edu.utn.frba.dds.agregador.models.domain.criterio;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "criterio")
public class Criterio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String detalle;

    @OneToMany(mappedBy = "criterio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntidadFiltro> filtros = new ArrayList<>();

    public Criterio(List<EntidadFiltro> filtros) {
        this.filtros = new ArrayList<>();
        if (filtros != null) {
            agregarFiltros(filtros);
        }
    }

    public Boolean coincideCon(Hecho hecho) {
        if (filtros == null || filtros.isEmpty()) return true;

        return filtros.stream().allMatch(filtro -> {
            try {
                return filtro.coincide(hecho);
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void agregarFiltro(EntidadFiltro filtro) {
        filtro.setCriterio(this);
        this.filtros.add(filtro);
    }

    public void agregarFiltros(Collection<? extends EntidadFiltro> filtros) {
        filtros.forEach(this::agregarFiltro);
    }

    public void setFiltros(List<EntidadFiltro> nuevosFiltros) {
        this.filtros.clear();
        if (nuevosFiltros != null) {
            agregarFiltros(nuevosFiltros);
        }
    }

    public void quitarFiltro(EntidadFiltro filtro) {
        this.filtros.remove(filtro);
        filtro.setCriterio(null);
    }
}