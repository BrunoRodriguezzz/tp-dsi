package ar.edu.utn.frba.dds.domain.models.entities.hechos;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.Criterio;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.Filtro;
import ar.edu.utn.frba.dds.domain.models.entities.fuentes.Fuente;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class Coleccion {
    private Long id;
    private String titulo;
    private String descripcion;
    private List<Fuente> fuentes;
    private Criterio criterio;
    private List<Hecho> hechos;

    public Coleccion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public List<Hecho> cargarHechos(List<Hecho> hechos) {
        this.hechos = this.filtrarHechosSegunCriterio(hechos);
        return this.hechos;
    }

    public boolean cargarHecho(Hecho hecho) {
        if (!hecho.getEstaEliminado() && cumpleCriterioColeccion(hecho)) {
            if (this.hechos == null) {
                this.hechos = new ArrayList<>();
            }
            return this.hechos.add(hecho);
        }
        return false;
    }

    // Consultas de Hechos
    public List<Hecho> consultarHechos() {
        if(this.hechos == null){
            return new ArrayList<>();
        }
        return this.hechos;
    }

    public List<Hecho> consultarHechos(List<Filtro> filtros) {
        List<Hecho> hechosQueCumplenFiltrosUsuario = this.aplicarFiltros(filtros);
        return hechosQueCumplenFiltrosUsuario;
    }

    public void agregarFiltroACriterio(Filtro filtro){
        this.criterio.agregarFiltro(filtro);
    }

    public List<Hecho> recalcularHechos(){
        List<Hecho> hechos = this.filtrarHechosSegunCriterio(this.hechos);
        this.hechos = hechos;
        return hechos;
    }

    // Auxiliares a consultas de Hechos
    private List<Hecho> filtrarHechosSegunCriterio(List<Hecho> hechos) {
        return hechos.stream()
            .filter(hecho -> this.cumpleCriterioColeccion(hecho) && !hecho.getEstaEliminado())
            .collect(Collectors.toList());
    }


    private Boolean cumpleCriterioColeccion(Hecho hecho){
    if(this.criterio == null){ //Si no tengo criterio, lo cumple
        return true;
    }
        return this.criterio.cumpleCriterio(hecho);
    }

    private List<Hecho> aplicarFiltros(List<Filtro> filtros) {
        List<Hecho> hechosQueCumplenFiltros = null;
        hechosQueCumplenFiltros = this.hechos
                .stream()
                .filter(hecho -> filtros.stream().allMatch(filtro -> {
                    try {
                        return filtro.coincide(hecho);
                    } catch (Exception e) {
                        //TODO: Throw exception acá y en todos los que devuelvan false para que lo agarre el que los llamo y eventualmente una capa superior
                        // throw new Exception("No se pudo aplicar los filtros de búsqueda")
                        // TODO: Catchea el controller?
                        System.out.println("Error en filtro: " + filtro.getClass().getSimpleName());
                        return false;
                    }
                }))
                .collect(Collectors.toList());
        return hechosQueCumplenFiltros;
    }

}