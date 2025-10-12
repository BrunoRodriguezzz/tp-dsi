package ar.edu.utn.frba.dds.servicioAutenticacion.domain.dtos;



import lombok.Data;

import java.time.LocalDate;

@Data
public class ContribuyenteInputDTO {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
}
