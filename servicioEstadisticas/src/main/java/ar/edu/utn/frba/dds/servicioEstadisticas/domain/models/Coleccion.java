package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "coleccion", uniqueConstraints = @UniqueConstraint(columnNames = "coleccion_detalle"))
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "coleccion_detalle", unique = true, nullable = false, columnDefinition = "VARCHAR(100)")
    String detalle;
}
