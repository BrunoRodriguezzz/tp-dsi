package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "solicitud")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "spam", nullable = false, columnDefinition = "BOOLEAN")
    Boolean spam;
}
