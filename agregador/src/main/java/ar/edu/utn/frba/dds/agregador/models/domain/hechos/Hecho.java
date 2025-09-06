package ar.edu.utn.frba.dds.agregador.models.domain.hechos;

import ar.edu.utn.frba.dds.agregador.converters.ConsensoConverter;
import ar.edu.utn.frba.dds.agregador.converters.OrigenConverter;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.DescripcionInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.TituloInvalidoException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ContenidoMultimedia;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Etiqueta;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Ubicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "fuente_interno_id")
    private Long idInternoFuente;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Ubicacion ubicacion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "etiquetas_x_hecho",
            joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id", referencedColumnName = "id")
    )
    private List<Etiqueta> etiquetas;

    @Column(name = "fecha_acontecimiento")
    private LocalDate fechaAcontecimiento;

    @Column(name = "fecha_carga")
    private LocalDate fechaCarga;

    @Column()
    @Convert(converter = OrigenConverter.class)
    private Origen origen;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Contribuyente contribuyente;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "hecho_consensos",
            joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    )
    @Column(name = "consenso")
    @Convert(converter = ConsensoConverter.class)
    private List<Consenso> consensos;

    @Column(name = "esta_eliminado")
    private Boolean estaEliminado;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Fuente fuente;

    @OneToMany
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    private List<ContenidoMultimedia> contenidoMultimedia;

    public Hecho (String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, Origen origen) throws FechaInvalidaException, TituloInvalidoException, DescripcionInvalidaException {
        if(titulo == null || titulo.isBlank()) throw new TituloInvalidoException("El título no puede estar vacío");
        if(descripcion == null || descripcion.isBlank()) throw new DescripcionInvalidaException("La descripcion no puede ser nula o vacía");
        if(fechaAcontecimiento == null) {throw new FechaInvalidaException("No se proveyó una fecha de acontecimiento");}

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
//        if(fechaAcontecimiento.isAfter(LocalDate.now())) {
//            throw new FechaInvalidaException("La fecha: " + fechaAcontecimiento + "es una fecha futura");
//        }
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.origen = origen;
        this.fechaCarga = null;
        this.consensos = new ArrayList<>();
    }

    public void eliminar() throws Exception {
        if(this.estaEliminado) {throw new Exception("El hecho ya fue eliminado");}
        this.estaEliminado = true;
    }

    // Metodos
    public Boolean tieneContenidoMultimedia() {
        return !this.contenidoMultimedia.isEmpty();
    }

    public Boolean agregarEtiqueta(Etiqueta etiqueta) {
        if(this.etiquetas == null) {this.etiquetas = new ArrayList<>();}
        if (this.etiquetas.stream().noneMatch(e -> e.getTitulo().equals(etiqueta.getTitulo()))){
            this.etiquetas.add(etiqueta);
            return true;
        }
        return false;
    }

    public void agregarConsenso(Consenso consenso) {
        this.consensos.add(consenso);
    }

    public boolean equals(Hecho hecho) {
        if (this == hecho) return true;
        if (hecho == null || getClass() != hecho.getClass()) return false;

        boolean resultado = this.titulo.equals(hecho.getTitulo()) &&
            this.descripcion.equals(hecho.getDescripcion()) &&
            this.categoria.getTitulo().equals(hecho.getCategoria().getTitulo()) &&
            this.ubicacion.getLatitud().equals(hecho.getUbicacion().getLatitud()) &&
            this.ubicacion.getLongitud().equals(hecho.getUbicacion().getLongitud()) &&
            this.fechaAcontecimiento.equals(hecho.fechaAcontecimiento);
        return resultado;
    }
}