package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import java.time.LocalDate;
import lombok.Data;

@Data
public class HechoInputDTO {
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionInputDTO ubicacion;
  private LocalDate fechaAcontecimiento;
  private String origen;
  private String fuente;
}
