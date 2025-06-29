package ar.edu.utn.frba.dds.agregador.models.domain.colecciones;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Coleccion {
    private Long id;
    private Long idInternoFuente;
    private String titulo;
    private String descripcion;
    private List<Fuente> fuentes;
    private Criterio criterio;
    private List<Consenso> consensos;
    private List<Hecho> hechos;

    public Coleccion(String titulo, String descripcion, List<Fuente> fuentes, Criterio criterio) {
        if (titulo == null || titulo.isEmpty()) {
            throw new RuntimeException("El título es obligatorio");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new RuntimeException("La descripcion es obligatoria");
        }
        if (fuentes == null || fuentes.isEmpty()) {
            throw new RuntimeException("Las fuentes son obligatorias");
        }
        if (criterio == null) {
            throw new RuntimeException("El criterio es obligatorio");
        }
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuentes = fuentes;
        this.criterio = criterio;
        this.hechos = new ArrayList<>();
    }

    public List<Hecho> cargarHechos(List<Hecho> hechos) {
        this.hechos.addAll(this.filtrarHechosSegunCriterioYFuentes(hechos));
        return this.hechos;
    }

    public void agregarFuente(Fuente fuente) {
        this.fuentes.add(fuente);
    }

    public void cambiarCriterio(Criterio criterio) {
        this.setCriterio(criterio);
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
    public List<Hecho> consultarHechosCurados() {
        if(this.hechos == null){
            return new ArrayList<>();
        }
        return this.hechos.stream().filter(h -> h
            .getConsensos().stream().allMatch(consensoHecho ->
                this.consensos.stream().anyMatch(consensoInterno ->
                    consensoInterno.equals(consensoHecho)
                    )
                )
        ).collect(Collectors.toList());
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
        List<Hecho> hechos = this.filtrarHechosSegunCriterioYFuentes(this.hechos);
        this.hechos = hechos;
        return hechos;
    }

    // Auxiliares a consultas de Hechos
    private List<Hecho> filtrarHechosSegunCriterioYFuentes(List<Hecho> hechos) {
        List<Long> idsFuentes = this.getFuentes().stream()
            .map(Fuente::getId)
            .collect(Collectors.toList());

        return hechos.stream()
            .filter(hecho ->
                this.cumpleCriterioColeccion(hecho) &&
                    !hecho.getEstaEliminado() &&
                    idsFuentes.contains(hecho.getFuente().getId())
            )
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
                        System.out.println("Error en filtro: " + filtro.getClass().getSimpleName());
                        return false;
                    }
                }))
                .collect(Collectors.toList());
        return hechosQueCumplenFiltros;
    }

}