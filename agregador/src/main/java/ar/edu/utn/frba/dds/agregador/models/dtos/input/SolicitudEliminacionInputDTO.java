package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SolicitudEliminacionInputDTO {
  private Long idHecho;
  private String fundamento;
  private LocalDateTime fechaCreacion;
  private ContribuyenteInputDTO contribuyente;
}
