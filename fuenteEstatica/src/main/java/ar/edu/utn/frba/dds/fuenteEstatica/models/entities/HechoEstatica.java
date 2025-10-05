package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.fuenteEstatica.models.enums.Origen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hecho")
public class HechoEstatica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Nuestro ID

    // TODO: hace falta una FK acá? o no es necesario?
    @Column(nullable = false)
    private Long idArchivo; // Es el ID de la fuente que es propietaria del hecho
    @Column(nullable = false)
    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    @Column(nullable = false)
    private String categoria;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    private Ubicacion ubicacion;

    @Column(nullable = false)
    private LocalDateTime fechaHecho;
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    @Column
    private LocalDateTime fechaModificacion;
    @Column(nullable = false)
    private Boolean eliminado;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Origen origen;

    public void eliminar() {
        this.eliminado = true;
    }

    public void establecerUbicacion(Double lat, Double lon) {
        try {
            this.ubicacion = new Ubicacion(lat.toString(), lon.toString());
        } catch (Exception e) {} //TODO
    }

    public Boolean cumpleFecha(LocalDateTime fecha) {
        return this.fechaModificacion.isAfter(fecha) || this.fechaCreacion.isAfter(fecha);
    }
}
