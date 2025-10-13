package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Coleccion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Provincia;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadistica_provincia_x_coleccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaProvinciaXColeccion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "coleccion_id")
  private Coleccion coleccion;

  private LocalDateTime fecha;

  @ElementCollection
  @CollectionTable(name = "provincia_hechos_coleccion")
  @MapKeyEnumerated(EnumType.STRING)
  private Map<Provincia, Long> provinciasConHechos;

  public EstadisticaProvinciaXColeccion(Map<Provincia, Long> provinciasConHechos, LocalDateTime fecha, Coleccion coleccion) {
    this.provinciasConHechos = provinciasConHechos;
    this.fecha = fecha;
    this.coleccion = coleccion;
  }
}

