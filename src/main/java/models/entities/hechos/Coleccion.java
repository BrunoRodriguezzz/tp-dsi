package models.entities.hechos;

import models.entities.criterios.Criterio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Coleccion {
    private String titulo;
    private String descripcion;
    private Criterio criterio; // TODO FIX Métodos
    private List<Hecho> hechos;

    public Coleccion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.hechos = new ArrayList<>();
    }

    public Integer eliminarHecho(Hecho hecho) {
        if (this.hechos.remove(hecho)) {
            return 1;
        }
        return 0;
    }

    public Integer agregarHecho(Hecho hecho) {
        if (this.cumpleCriterio(hecho)
                && !hecho.getEliminado()) {
            this.hechos.add(hecho);
            return 1;
        }
        return 0;
    }

    public List<Hecho> consultarHechos() {
        this.recalcularHechos();
        return this.hechos;
    }

    public List<Hecho> consultarHechos(Criterio criterio) {
        this.recalcularHechos();
        List<Hecho> hechosQueCumplen = this.hechos
                .stream()
                .filter(hecho -> criterio.cumpleCriterio(hecho))
                .collect(Collectors.toList());
        return hechosQueCumplen;
    }

    private Boolean cumpleCriterio(Hecho hecho){
    if(this.criterio == null){
        return true;
    }
    return this.criterio.cumpleCriterio(hecho);
    }

    public Integer setCriterio(Criterio criterio) {
        this.criterio = criterio;
        return 1;
    }

    private void recalcularHechos() {  //TODO: Cambiar en plant (void)
        if (!this.hechos.isEmpty()) {
            this.hechos.removeIf(hecho -> !this.cumpleCriterio(hecho));
        }
    }
}