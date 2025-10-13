package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResolucionSolicitudEliminacionInputDTO {
  private AdministradorInputDTO administrador;
  private LocalDateTime fechaResolucion;
}
