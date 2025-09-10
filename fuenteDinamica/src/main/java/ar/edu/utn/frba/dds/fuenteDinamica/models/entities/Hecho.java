package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import ar.edu.utn.frba.dds.fuenteDinamica.converters.estadoConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
public class Hecho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contribuyente_id",referencedColumnName = "id")
    private Contribuyente contribuyente;

    @Column(name = "titulo", nullable = false, columnDefinition = "TEXT")
    private String titulo;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    private Categoria categoria;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    private List<ContenidoMultimedia> contenidoMultimedia;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    private Ubicacion ubicacion;

    @Column(name = "fecha_acontecimiento",nullable = false)
    private LocalDate fechaAcontecimiento;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "etiquetas_x_hecho",
            joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id", referencedColumnName = "id")
    )
    private List<Etiqueta> etiquetas;

    @Column(name = "fecha_guardado")
    private LocalDateTime fechaGuardado;

    @Column(name = "fecha_de_modicacion")
    private LocalDateTime fechaModificacion;

    @Column
    @Convert(converter = estadoConverter.class)
    private EstadoHecho estadoHecho;

    @Column(name = "sugerencia_de_cambio")
    private String sugerenciaDeCambio;

    @Column(name = "enviado")
    private Boolean enviado;

    @Column(name = "eliminado")
    private Boolean estaEliminado;

    @Column(name = "origen")
    private String origen;

    @Column(name = "fuente")
    private String fuente;
}
