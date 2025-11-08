package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Hecho;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.SolicitudEliminacion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class SolicitudEliminacionInputDTO {
  private Long id;
  private HechoInputDTO hecho;
  private String fundamento;
  private LocalDateTime fechaCreacion;
  private ContribuyenteInputDTO contribuyente;
  private String estado;
  private ResolucionSolicitudEliminacionInputDTO resolucion;
  
  public SolicitudEliminacion convertirASolicitud(Hecho hecho) {
    SolicitudEliminacion solicitud = new SolicitudEliminacion();
    solicitud.setId(this.id);
    solicitud.setHecho(hecho);
    solicitud.setEstado(EstadoSolicitudEliminacion.valueOf(this.estado));

    return solicitud;
  }
}
