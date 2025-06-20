package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ResolucionSolicitudEliminacionOutputDTO {
  private AdministradorOutputDTO administrador;
  private LocalDateTime fechaResolucion;
}
