package entities;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Coleccion {
    private String titulo;
    private String descripcion;
    private Criterio criterio;
    private Set<Hecho> hechos;

    public Integer eliminarHecho(Hecho hecho) {
        if (this.hechos.remove(hecho)) {
            return 1;
        }
        return 0;
    }

    public Integer agregarHecho(Hecho hecho) {
        if (this.hechos.add(hecho)) {
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
}