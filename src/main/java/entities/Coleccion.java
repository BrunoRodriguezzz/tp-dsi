package entities;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Coleccion {
    private String titulo;
    private String descripcion;
    private List<Criterio> criterios;
    private Set<Hecho> hechos;

    public Integer eliminarHecho(Hecho hecho) {
        if (this.hechos.remove(hecho)) {
            return 1;
        }
        return 0;
    }

    public Integer agregarHecho(Hecho hecho) {
        if (this.cumpleTodosLosCriterios(hecho)
                && !hecho.getEliminado()
                && this.hechos.add(hecho)) {
            return 1;
        }
        return 0;
    }

    public Set<Hecho> consultarHechos() {
        return this.hechos;
    }

    public Set<Hecho> consultarHechos(Criterio criterio) {
        Set<Hecho> hechosQueCumplen = this.hechos
                .stream()
                .filter(hecho -> criterio.cumpleCriterio(hecho))
                .collect(Collectors.toSet());
        return hechosQueCumplen;
    }

    private Boolean cumpleTodosLosCriterios(Hecho hecho) {
        return this.criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }
}