package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.HoraDelDia;
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
@Table(name = "estadistica_hora_x_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaHoraXCategoria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "categoria_id")
  private Categoria categoria;

  private LocalDateTime fecha;

  @ElementCollection
  @CollectionTable(name = "hora_hechos_categoria")
  @MapKeyEnumerated(EnumType.STRING)
  private Map<HoraDelDia, Long> horasConHechos;

  public EstadisticaHoraXCategoria(Categoria categoria, LocalDateTime fecha, Map<HoraDelDia, Long> horasConHechos) {
    this.categoria = categoria;
    this.fecha = fecha;
    this.horasConHechos = horasConHechos;
  }
}
