package ar.edu.utn.frba.dds.agregador.models.domain.criterio;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class Criterio {
    private List<Filtro> filtros;

    public Criterio() {
        this.filtros = new ArrayList<>();
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