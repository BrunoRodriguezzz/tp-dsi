package ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  public EstadisticaSolicitudes(LocalDateTime fecha, Long solicitudes_spam, Long solicitudes_no_spam) {
      this.fecha = fecha;
      this.solicitudes_spam = solicitudes_spam;
      this.solicitudes_no_spam = solicitudes_no_spam;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  Long id;

  @Column(nullable = false, columnDefinition = "TIMESTAMP")
  LocalDateTime fecha;

  @Column(nullable = false, columnDefinition = "BIGINT")
  Long solicitudes_spam;

  @Column(nullable = false, columnDefinition = "BIGINT")
  Long solicitudes_no_spam;
}
