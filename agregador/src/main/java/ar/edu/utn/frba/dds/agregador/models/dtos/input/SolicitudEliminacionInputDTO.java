package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SolicitudEliminacionInputDTO {
  private HechoInputDTO hecho;
  private String fundamento;
  private LocalDate fechaCreacion;
  private ContribuyenteInputDTO contribuyente;
}
