package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estadistica_solicitudes")
public class EstadisticaSolicitudes {
  public EstadisticaSolicitudes(LocalDateTime fecha, Integer solicitudes_spam) {
      this.fecha = fecha;
      this.solicitudes_spam = solicitudes_spam;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false, columnDefinition = "TIMESTAMP")
  LocalDateTime fecha;

  @Column(nullable = false, columnDefinition = "BIGINT")
  Integer solicitudes_spam;
}
