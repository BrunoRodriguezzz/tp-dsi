package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output;

import lombok.Data;

@Data
public class ContribuyenteDTO {
  private String nombre;
  private String apellido;
  private String fechaNacimiento;
  private Long id;
}
