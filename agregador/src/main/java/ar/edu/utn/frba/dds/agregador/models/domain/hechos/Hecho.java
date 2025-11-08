package ar.edu.utn.frba.dds.agregador.models.domain.hechos;

import ar.edu.utn.frba.dds.agregador.converters.ConsensoConverter;
import ar.edu.utn.frba.dds.agregador.converters.OrigenConverter;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.HechoYaEliminadoException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.DescripcionInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.TituloInvalidoException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ContenidoMultimedia;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Etiqueta;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;

import java.time.LocalDate;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<HechoFuente> fuenteSet;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(referencedColumnName = "id")
    private Categoria categoria;

    @Embedded
    private Ubicacion ubicacion;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "etiquetas_x_hecho",
            joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id", referencedColumnName = "id")
    )
    private List<Etiqueta> etiquetas;

    @Column(name = "fecha_acontecimiento")
    private LocalDateTime fechaAcontecimiento;

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    private List<ContenidoMultimedia> contenidoMultimedia;

    public Hecho (String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fechaAcontecimiento, Origen origen) throws FechaInvalidaException, TituloInvalidoException, DescripcionInvalidaException {
        if (titulo == null || titulo.isBlank())
            throw new TituloInvalidoException("El título no puede estar vacío");
        if (descripcion == null || descripcion.isBlank())
            throw new DescripcionInvalidaException("La descripcion no puede ser nula o vacía");
        if (fechaAcontecimiento == null) {
            throw new FechaInvalidaException("No se proveyó una fecha de acontecimiento");
        }

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.origen = origen;
        this.fechaCarga = null;
        this.consensos = new ArrayList<>();
        this.fuenteSet = new HashSet<>();
    }

    public void eliminar() {
        if(this.estaEliminado) throw new HechoYaEliminadoException("El hecho ya fue eliminado", this);
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

    public void agregarFuente(Fuente fuente, Long idHechoEnFuente) {
        if (this.fuenteSet.stream().noneMatch(e -> e.getFuente().getNombre().equals(fuente.getNombre()))) {
            HechoFuente hechoFuente = new HechoFuente();
            hechoFuente.setHecho(this);
            hechoFuente.setFuente(fuente);
            hechoFuente.setIdInternoFuente(idHechoEnFuente);
            this.fuenteSet.add(hechoFuente);
        }
    }

    public Long getIdInternoFuente(Fuente fuente) {
        return this.fuenteSet
                .stream()
                .filter(hechoFuente -> hechoFuente.getFuente().equals(fuente))
                .findFirst()
                .map(HechoFuente::getId)
                .orElse(null);
    }

    public void quitarFuente(Fuente fuente, Long idHechoEnFuente) {
        this.fuenteSet
                .removeIf(hf -> hf.getFuente().equals(fuente));
    }

    public void agregarConsenso(Consenso consenso) {
        this.consensos.add(consenso);
    }

    public void actualizarDesdeDTO(HechoInputDTO dto)  {
        if (dto.getTitulo() != null) this.titulo = dto.getTitulo();
        if (dto.getDescripcion() != null) this.descripcion = dto.getDescripcion();
        if (dto.getUbicacion() != null) {
            this.ubicacion = new Ubicacion(
                Double.parseDouble(dto.getUbicacion().getLatitud()),
                Double.parseDouble(dto.getUbicacion().getLongitud())
            );
        }
        if (dto.getEtiquetas() != null) {
            this.etiquetas = dto.getEtiquetas().stream()
                .map(Etiqueta::new)
                .collect(java.util.stream.Collectors.toList());
        }
        if (dto.getFechaAcontecimiento() != null) this.fechaAcontecimiento = dto.getFechaAcontecimiento();
        if (dto.getOrigen() != null) this.origen = Origen.valueOf(dto.getOrigen().toUpperCase());
        if (dto.getContribuyente() != null) {
            this.contribuyente = new Contribuyente(
                dto.getContribuyente().getNombre(),
                dto.getContribuyente().getApellido(),
                dto.getContribuyente().getFechaNacimiento()
            );
        }
        if (dto.getContenidoMultimedia() != null) {
            this.contenidoMultimedia = dto.getContenidoMultimedia().stream()
                .map(ContenidoMultimedia::new)
                .collect(java.util.stream.Collectors.toList());
        }
        // No se actualizan consensos ni estaEliminado desde el DTO por seguridad
    }

    public boolean equals(Hecho hecho) {
        if (this == hecho) return true;
        if (hecho == null || getClass() != hecho.getClass()) return false;

        return this.titulo.equals(hecho.getTitulo()) &&
            this.descripcion.equals(hecho.getDescripcion()) &&
            this.categoria.getTitulo().equals(hecho.getCategoria().getTitulo()) &&
            this.ubicacion.getLatitud() == hecho.getUbicacion().getLatitud() &&
            this.ubicacion.getLongitud() == hecho.getUbicacion().getLongitud() &&
            this.fechaAcontecimiento.equals(hecho.fechaAcontecimiento);
    }
}