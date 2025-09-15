package ar.edu.utn.frba.dds.agregador.models.domain.criterio;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "criterio")
public class Criterio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private List<Filtro> filtros;

    public Criterio() {
        this.filtros = new ArrayList<>();
    }

    public Criterio(List<Filtro> filtros) {
        this.filtros = filtros;
    }

    public Boolean cumpleCriterio(Hecho hecho) {
        Boolean resultado = false;
        resultado = this.filtros.stream().allMatch(filtro ->
            {
                try {
                    return filtro.coincide(hecho);
                } catch (Exception e) {
                    // TODO: Catchea el controller?
                    throw new RuntimeException(e);
                }
            }
        );
        return resultado;
    }

    public void agregarFiltro(Filtro filtro) {
        this.filtros.add(filtro);
    }
}