
package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "categoria", uniqueConstraints = @UniqueConstraint(columnNames = "categoria_detalle"))
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "categoria_detalle", unique = true, nullable = false, columnDefinition = "VARCHAR(100)")
    String detalle;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        Categoria that = (Categoria) obj;
        return Objects.equals(detalle, that.detalle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(detalle);
    }
}
