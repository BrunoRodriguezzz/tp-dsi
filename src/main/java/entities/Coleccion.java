package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Coleccion {
    private String titulo;
    private String descripcion;
    private List<Criterio> criterios;
    private List<Hecho> hechos;
    private List<ImportadorHechos> fuentes;

    public Coleccion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios = new ArrayList<>();
        this.hechos = new ArrayList<>();
        this.fuentes = new ArrayList<>();
    }

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

    public List<Hecho> consultarHechos() {
        return this.hechos;
    }

    public List<Hecho> consultarHechos(Criterio criterio) {
        List<Hecho> hechosQueCumplen = this.hechos
                .stream()
                .filter(hecho -> criterio.cumpleCriterio(hecho))
                .collect(Collectors.toList());
        return hechosQueCumplen;
    }

    private Boolean cumpleTodosLosCriterios(Hecho hecho) {
        return this.criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }

    public Integer agregarCriterio(Criterio criterio) {
        this.criterios.add(criterio);
        return 1;
    }

    public Integer recalcularHechos() {
        if (hechos == null) {
            return 0;
        }
        // Usar removeIf para eliminar hechos que no cumplen los criterios
        this.hechos.removeIf(hecho -> !this.cumpleTodosLosCriterios(hecho));
        return 1;
    }

}