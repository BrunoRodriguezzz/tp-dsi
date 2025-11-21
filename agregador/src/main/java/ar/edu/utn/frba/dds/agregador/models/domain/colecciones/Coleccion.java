package ar.edu.utn.frba.dds.agregador.models.domain.colecciones;

import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.FiltroMapper;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coleccion")
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fuente_interno_id")
    private Long idInternoFuente;

    @Column(nullable = false)
    private String titulo;
    @Column
    private String descripcion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "fuente_x_coleccion",
            joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id")
    )
    private List<Fuente> fuentes = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "criterio_id")
    private Criterio criterio;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "consensos_x_coleccion",
            joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id")
    )
    @Column(name = "consenso")
    @Enumerated(EnumType.STRING) // estaba esto --> @Convert(converter = OrigenConverter.class)
    private List<Consenso> consensos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hecho_x_coleccion",
            joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    )
    private List<Hecho> hechos = new ArrayList<>();

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
        this.consensos = new ArrayList<>();
    }

    public List<Hecho> cargarHechos(List<Hecho> hechos) {
        List<Hecho> filtrados = this.filtrarHechosSegunCriterioYFuentes(hechos);
        System.out.println("Hechos después de filtrar: " + (filtrados != null ? filtrados.size() : 0));

        if (filtrados == null || filtrados.isEmpty()) {
            return this.hechos;
        }
        for (Hecho h : filtrados) {
            this.cargarHecho(h);
        }
        return this.hechos;
    }

    public void agregarFuente(Fuente fuente) {
        this.fuentes.add(fuente);
    }

    public void agregarConsenso(Consenso consenso) {
        if(!this.consensos.contains(consenso)){
            this.consensos.add(consenso);
        }
    }

    public void cambiarCriterio(Criterio criterio) {
        this.setCriterio(criterio);
        this.recalcularHechos();
    }

    public boolean cargarHecho(Hecho hecho) {
        if (hecho == null || hecho.getEstaEliminado()) {
            return false;
        }
        if (!cumpleCriterioColeccion(hecho)) {
            return false;
        }
        if (this.hechos == null) {
            this.hechos = new ArrayList<>();
        }
        if (hecho.getId() != null) {
            boolean existe = this.hechos.stream().anyMatch(h -> h.getId() != null && h.getId().equals(hecho.getId()));
            if (existe) return false;
        } else {
            boolean existeObj = this.hechos.stream().anyMatch(h -> h == hecho || (h.getTitulo() != null && h.getTitulo().equals(hecho.getTitulo()) && h.getFechaAcontecimiento() != null && h.getFechaAcontecimiento().equals(hecho.getFechaAcontecimiento())));
            if (existeObj) return false;
        }
        return this.hechos.add(hecho);
    }

    public List<Hecho> consultarHechosCurados(List<Filtro> filtros) {
        List<Hecho> hechosQueCumplenFiltrosUsuario = this.aplicarFiltros(filtros);
        if(hechosQueCumplenFiltrosUsuario == null){
            return new ArrayList<>();
        }
        return this.filtrarCurados(hechosQueCumplenFiltrosUsuario);
    }

    public List<Hecho> consultarHechosCurados() {
        return this.filtrarCurados(this.hechos);
    }

    private List<Hecho> filtrarCurados(List<Hecho> hechos) {
        return hechos.stream().filter(h -> {
                if(h.getConsensos().isEmpty()){
                    return false;
                }
                else
                    return h.getConsensos().stream().allMatch(consensoHecho ->
                    this.consensos.stream().anyMatch(consensoInterno ->
                        consensoInterno.equals(consensoHecho))
                );
            }
        ).collect(Collectors.toList());
    }

    public List<Hecho> consultarHechos() {
        if(this.hechos == null){
            return new ArrayList<>();
        }
        return this.hechos;
    }

    public List<Hecho> consultarHechos(List<Filtro> filtros) {
        return this.aplicarFiltros(filtros);
    }

    public void agregarFiltroACriterio(Filtro filtro){
        FiltroMapper filtroMapper = new FiltroMapper();
        this.criterio.agregarFiltro(filtroMapper.toEntity(filtro));
        this.recalcularHechos();
    }

    public List<Hecho> recalcularHechos(){
        List<Hecho> hechos = this.filtrarHechosSegunCriterioYFuentes(this.hechos);
        this.hechos = hechos;
        return hechos;
    }

    private List<Hecho> filtrarHechosSegunCriterioYFuentes(List<Hecho> hechos) {
        List<Long> IDfuentes = this.getFuentes()
                .stream()
                .map(Fuente::getId)
                .toList();

        System.out.println("Filtros del criterio:");
        if (this.criterio != null && this.criterio.getFiltros() != null) {
            this.criterio.getFiltros().forEach(f ->
                System.out.println("  - " + f.getClass().getSimpleName())
            );
        }

        List<Hecho> resultado = hechos.stream()
            .filter(hecho -> {
                boolean cumpleCriterio = this.cumpleCriterioColeccion(hecho);
                boolean noEliminado = !hecho.getEstaEliminado();
                boolean esDeFuente = hecho.getFuenteSet()
                        .stream()
                        .map(hechoFuente -> hechoFuente.getFuente().getId())
                        .anyMatch(IDfuentes::contains);

                boolean pasa = cumpleCriterio && noEliminado && esDeFuente;

                if (!pasa && !cumpleCriterio) {
                    System.out.println("Hecho rechazado por criterio: " + hecho.getTitulo());
                }

                return pasa;
            })
            .collect(Collectors.toList());

        return resultado;
    }

    private Boolean cumpleCriterioColeccion(Hecho hecho) {
    if(this.criterio == null) { //Si no tengo criterio es q lo cumple
        return true;
    }
        return this.criterio.coincideCon(hecho);
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