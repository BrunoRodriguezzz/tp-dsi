package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import java.time.LocalDate;
import lombok.Data;

@Data
public class HechoOutputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionOutputDTO ubicacion;
  private LocalDate fechaAcontecimiento;
  private ContribuyenteOutputDTO contribuyente;
  private String fuente;
  private String origen;
}
