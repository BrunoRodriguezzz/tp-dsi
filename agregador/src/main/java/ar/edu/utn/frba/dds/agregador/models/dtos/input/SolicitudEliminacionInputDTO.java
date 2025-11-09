package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SolicitudEliminacionInputDTO {
  private Long idHecho;
  private String fundamento;
  private LocalDateTime fechaCreacion;
  private Long idContribuyente;

  public static SolicitudEliminacion DTOtoSolicitud (SolicitudEliminacionInputDTO solicitudDTO, Hecho hecho, Contribuyente c){
    Contribuyente contribuyente = null;
    SolicitudEliminacion solicitud = null;
    try{
      if(c != null) {
        contribuyente = new Contribuyente(
            c.getNombre(),
            c.getApellido(),
            c.getFechaNacimiento()
        );
      }
      solicitud = new SolicitudEliminacion(
          hecho,
          solicitudDTO.getFundamento(),
          contribuyente,
          solicitudDTO.getFechaCreacion()
      );
      solicitud.setEstadoSolicitudEliminacion(EstadoSolicitudEliminacion.PENDIENTE);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return solicitud;
  }
}
