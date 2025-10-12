package ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "coleccion", uniqueConstraints = @UniqueConstraint(columnNames = "coleccion_detalle"))
public class Coleccion {
    @Id
    Long id;

    @Column(name = "coleccion_detalle", unique = true, nullable = false, columnDefinition = "VARCHAR(100)")
    String detalle;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        Coleccion that = (Coleccion) obj;
        return Objects.equals(this.detalle, that.detalle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(detalle);
    }
}
