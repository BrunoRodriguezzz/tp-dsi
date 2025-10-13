package ar.edu.utn.frba.dds.servicioAutenticacion.domain.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "solicitud_eliminacion")
public class SolicitudEliminacion {
    @Id
    Long id;

    @Enumerated(EnumType.STRING)
    EstadoSolicitudEliminacion estado;

    @OneToOne
    Hecho hecho;
}
