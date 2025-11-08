package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ResolucionSolicitudEliminacionInputDTO {
  private AdministradorInputDTO administrador;
  private LocalDateTime fechaResolucion;
}
