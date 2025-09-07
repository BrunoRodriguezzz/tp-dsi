package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.enums.Origen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hecho")
public class HechoProxy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Nuestro ID

    @Column(nullable = false)
    private Long idExterno; // Es el ID que nos da el propietario del Hecho

    @Column(nullable = false)
    private Long idFuente; // Es el ID de la fuente que es propietaria del hecho

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private String categoria;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Ubicacion ubicacion;

    @Column(nullable = false)
    private LocalDate fechaHecho;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaModificacion;

    @Column(nullable = false)
    private Boolean eliminado;

    @Column(nullable = false)
    private Origen origen;

    public HechoProxy(Long id, String titulo) {
        this.id = null;
        this.idExterno = id;
        this.titulo = titulo;
    }

    public void establecerUbicacion(Double lat, Double lon) {
        try {
            this.ubicacion = new Ubicacion(lat.toString(), lon.toString());
        } catch (Exception e) {} //TODO
    }

    public void eliminar() {
        this.eliminado = true;
    }

    public Boolean cumpleFecha(LocalDateTime fecha) {
        return this.fechaModificacion.isAfter(fecha) || this.fechaCreacion.isAfter(fecha);
    }
}
