package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.ResolucionSolicitudEliminacion;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ResolucionSolicitudEliminacionOutputDTO {
  private AdministradorOutputDTO administrador;
  private LocalDateTime fechaResolucion;

  public static ResolucionSolicitudEliminacionOutputDTO ResolucionToDTO (ResolucionSolicitudEliminacion resolucion) {
    ResolucionSolicitudEliminacionOutputDTO resolucionDTO = new ResolucionSolicitudEliminacionOutputDTO();
    resolucionDTO.setFechaResolucion(resolucion.getFechaResolucion());
    resolucionDTO.setAdministrador(AdministradorOutputDTO.AdministradorToDTO(resolucion.getAdministrador()));
    return resolucionDTO;
  }
}
