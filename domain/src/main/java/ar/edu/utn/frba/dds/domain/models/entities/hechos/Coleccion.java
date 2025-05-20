package ar.edu.utn.frba.dds.domain.models.entities.hechos;

import lombok.Getter;
import lombok.Setter;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.Criterio;
import ar.edu.utn.frba.dds.domain.models.entities.criterio.Filtro;
import ar.edu.utn.frba.dds.domain.models.entities.fuentes.Fuente;

import java.util.List;
import java.util.stream.Collectors;

public class Coleccion {
    private String titulo;
    private String descripcion;
    @Setter
    private Fuente fuente;
    @Setter
    private Criterio criterio;
    @Getter
    private List<Hecho> hechos;

    public Coleccion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public List<Hecho> cargarHechos(List<Hecho> hechos) {
        this.hechos = this.filtrarHechosSegunCriterio(hechos);
        return this.hechos;
    }

    // Consultas de Hechos
    public List<Hecho> consultarHechos() {
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
        List<Hecho> hechosQueCumplenFiltrosUsuario = this.filtrarHechosSegunCriterio(this.hechos);
        this.hechos = hechosQueCumplenFiltrosUsuario;
        return hechosQueCumplenFiltrosUsuario;
    }

    // Auxiliares a consultas de Hechos
    private List<Hecho> filtrarHechosSegunCriterio(List<Hecho> hechos) {
        List<Hecho> hechosFiltrados = hechos.stream().filter(hecho ->
            this.cumpleCriterioColeccion(hecho) && !hecho.getEstaEliminado()
        ).toList();

        return hechosFiltrados;
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