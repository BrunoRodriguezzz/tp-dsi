package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Provincia;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "estadistica_provincia_x_categoria")
public class EstadisticaProvinciaXCategoria {
  public EstadisticaProvinciaXCategoria(Categoria categoria, LocalDateTime fecha, Provincia provincia) {
    this.categoria = categoria;
    this.fecha = fecha;
    this.provincia = provincia;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  Long id;

  @Column(nullable = false, columnDefinition = "TIMESTAMP")
  LocalDateTime fecha;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "categoria_id")
  Categoria categoria;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "provincia_mas_hechos")
  Provincia provincia;
}
