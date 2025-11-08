package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ContribuyenteInputDTO {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
}
