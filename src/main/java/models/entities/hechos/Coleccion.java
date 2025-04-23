package models.entities.hechos;

import lombok.Setter;
import models.entities.criterio.Criterio;
import models.entities.criterio.Filtro;

import java.util.List;
import java.util.stream.Collectors;

public class Coleccion {
    private String titulo;
    private String descripcion;
    @Setter
    private Fuente fuente;
    @Setter
    private Criterio criterio;

    public Coleccion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }
    // Consultas de Hechos
    public List<Hecho> consultarHechos() {
        List<Hecho> hechos = fuente.importarHechos();
        hechos = this.filtrarHechosSegunCriterio(hechos);
        return hechos;
    }

    public List<Hecho> consultarHechos(List<Filtro> filtros) {
        List<Hecho> hechos = this.consultarHechos();
        List<Hecho> hechosQueCumplenFiltrosUsuario = this.aplicarFiltros(hechos, filtros);

        return hechosQueCumplenFiltrosUsuario;
    }

    // Auxiliares a consultas de Hechos
    private List<Hecho> filtrarHechosSegunCriterio(List<Hecho> hechos) {
        List<Hecho> hechosFiltrado = hechos.stream().filter( hecho ->
            this.cumpleCriterioColeccion(hecho) && !hecho.getEstaEliminado()
        ).toList();

        return hechosFiltrado;
    }

    private Boolean cumpleCriterioColeccion(Hecho hecho){
    if(this.criterio == null){ //Si no tengo criterio, lo cumple
        return true;
    }
        return this.criterio.cumpleCriterio(hecho);
    }

    private List<Hecho> aplicarFiltros(List<Hecho> hechos, List<Filtro> filtros) {
        List<Hecho> hechosQueCumplenFiltros = hechos
                .stream()
                .filter(hecho -> filtros.stream().allMatch(filtro -> filtro.coincide(hecho)))
                .collect(Collectors.toList());
        return hechosQueCumplenFiltros;
    }
}