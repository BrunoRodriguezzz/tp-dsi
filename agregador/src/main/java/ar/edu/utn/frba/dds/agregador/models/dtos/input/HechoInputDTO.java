package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import java.time.LocalDate;
import lombok.Data;

@Data
public class HechoInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionInputDTO ubicacion;
  private LocalDate fechaAcontecimiento;
  private ContribuyenteInputDTO contribuyente;
  private String origen;
  private String fuente;
}
