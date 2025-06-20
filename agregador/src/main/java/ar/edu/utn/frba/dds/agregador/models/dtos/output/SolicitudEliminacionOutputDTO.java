package ar.edu.utn.frba.dds.agregador.models.dtos.output;

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
}
