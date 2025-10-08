package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Categoria;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadistica_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaCategoria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime fecha;

  @ElementCollection
  @CollectionTable(name = "categoria_hechos")
  @MapKeyJoinColumn(name = "categoria_id")
  private Map<Categoria, Long> categoriasConHechos;

  public EstadisticaCategoria(Map<Categoria, Long> categoriasConHechos, LocalDateTime fecha) {
    this.categoriasConHechos = categoriasConHechos;
    this.fecha = fecha;
  }
}
