package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categoria", uniqueConstraints = @UniqueConstraint(columnNames = "categoria_detalle"))
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "categoria_detalle", unique = true, nullable = false, columnDefinition = "VARCHAR(100)")
    String detalle;
}
