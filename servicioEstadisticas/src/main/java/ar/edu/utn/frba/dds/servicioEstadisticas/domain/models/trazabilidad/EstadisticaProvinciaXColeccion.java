package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Coleccion;
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
@Table(name = "estadistica_provincia_x_coleccion")
public class EstadisticaProvinciaXColeccion {
  public EstadisticaProvinciaXColeccion(Provincia provincia, LocalDateTime fecha, Coleccion coleccion) {
    this.fecha = fecha;
    this.coleccion = coleccion;
    this.provincia = provincia;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  Long id;

  @Column(nullable = false, columnDefinition = "TIMESTAMP")
  LocalDateTime fecha;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "coleccion_id")
  Coleccion coleccion;

  @Column(nullable = false, name = "provincia_mas_hechos")
  @Enumerated(EnumType.STRING)
  Provincia provincia;
}
