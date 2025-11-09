package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.SolicitudEliminacion;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SolicitudEliminacionOutputDTO {
  private Long id;
  private HechoOutputDTO hecho;
  private String fundamento;
  private LocalDateTime fechaCreacion;
  private ContribuyenteOutputDTO contribuyente;
  private String estado;
  private ResolucionSolicitudEliminacionOutputDTO resolucion;

  public static SolicitudEliminacionOutputDTO SolicitudToDTO(SolicitudEliminacion solicitud){
    SolicitudEliminacionOutputDTO solicitudDTO = new SolicitudEliminacionOutputDTO();
    solicitudDTO.setId(solicitud.getId());
    solicitudDTO.setHecho(HechoOutputDTO.HechoToDTO(solicitud.getHecho()));
    solicitudDTO.setFundamento(solicitud.getFundamento());
    solicitudDTO.setFechaCreacion(solicitud.getFechaCreacion());
    if(solicitudDTO.getContribuyente() != null){
      solicitudDTO.setContribuyente(ContribuyenteOutputDTO.ContribuyenteToDTO(solicitud.getContribuyente()));
    }
    solicitudDTO.setEstado(solicitud.getEstadoSolicitudEliminacion().name());
    if(solicitud.getResolucionSolicitudEliminacion() != null){
      solicitudDTO.setResolucion(ResolucionSolicitudEliminacionOutputDTO.ResolucionToDTO(solicitud.getResolucionSolicitudEliminacion()));
    }
    return solicitudDTO;
  }
}
