package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ContribuyenteInputDTO {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
}
