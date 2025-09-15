package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Categoria;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estadistica_categoria")
public class EstadisticaCategoria {
  public EstadisticaCategoria(Categoria categoria, LocalDateTime fecha) {
    this.categoria = categoria;
    this.fecha = fecha;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "categoria_mas_hechos")
  Categoria categoria;

  @Column(nullable = false, columnDefinition = "TIMESTAMP")
  LocalDateTime fecha;
}
