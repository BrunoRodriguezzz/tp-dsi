package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import ar.edu.utn.frba.dds.fuenteDinamica.converters.estadoConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitudModificado")
public class SolicitudModificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hecho_id")
    private Long idHecho;

    @Column
    private String titulo;

    @Column
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    private Categoria categoria;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "solicitud_id", referencedColumnName = "id")
    private List<ContenidoMultimedia> contenidoMultimedia;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    private Ubicacion ubicacion;

    @Column(name = "fecha_acontecimiento")
    private LocalDateTime fechaAcontecimiento;

    @Column(name = "estado")
    @Convert(converter = estadoConverter.class)
    private EstadoHecho estadoSolicitud;

}
