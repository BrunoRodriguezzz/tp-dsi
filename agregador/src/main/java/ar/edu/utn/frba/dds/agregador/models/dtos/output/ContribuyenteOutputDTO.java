package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ContribuyenteOutputDTO {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
}
