package models.entities.hechos;

import lombok.Setter;
import models.entities.criterios.Criterio;
import models.entities.criterios.Filtro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Coleccion {
    private String titulo;
    private String descripcion;
    @Setter
    private Criterio criterio; // TODO FIX Métodos
    private List<Hecho> hechos;

    public Coleccion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.hechos = new ArrayList<>();
    }

    public Boolean eliminarHecho(Hecho hecho) {
        if (this.hechos.remove(hecho)) {
            return true;
        }
        return false;
    }

    public Boolean agregarHecho(Hecho hecho) {
        if (this.cumpleCriterio(hecho)
                && !hecho.getEliminado()) {
            this.hechos.add(hecho);
            return true;
        }
        return false;
    }

    public List<Hecho> consultarHechos() {
        this.recalcularHechos();
        return this.hechos;
    }

    public List<Hecho> consultarHechos(List<Filtro> filtros) {
        this.recalcularHechos();
        List<Hecho> hechosQueCumplen = this.hechos
                .stream()
                .filter(hecho -> filtros.stream().allMatch(filtro -> filtro.coincide(hecho)))
                .collect(Collectors.toList());
        return hechosQueCumplen;
    }

    private Boolean cumpleCriterio(Hecho hecho){
    if(this.criterio == null){
        return true;
    }
        return this.criterio.cumpleCriterio(hecho);
    }

    private void recalcularHechos() {
        if (!this.hechos.isEmpty()) {
            this.hechos = this.hechos.stream().filter(hecho -> this.cumpleCriterio(hecho)).toList();
//            this.hechos.removeIf(hecho -> !this.cumpleCriterio(hecho));
        }
    }
}