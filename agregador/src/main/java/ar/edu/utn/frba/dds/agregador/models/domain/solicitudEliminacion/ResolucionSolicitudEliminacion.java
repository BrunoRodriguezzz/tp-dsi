package ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "res_Sol_eliminacion")
public class ResolucionSolicitudEliminacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", referencedColumnName = "id")
    private Administrador administrador;

    @Column(nullable = false)
    private LocalDateTime fechaResolucion;

    public ResolucionSolicitudEliminacion(Administrador administrador, LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
        this.administrador = administrador;
    }

}
