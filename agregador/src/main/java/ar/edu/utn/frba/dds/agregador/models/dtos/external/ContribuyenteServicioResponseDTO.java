package ar.edu.utn.frba.dds.agregador.models.dtos.external;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ContribuyenteServicioResponseDTO {
  private Long id;
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
}
