package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HechoInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionInputDTO ubicacion;
  private LocalDate fechaAcontecimiento;
  private ContribuyenteInputDTO contribuyente;
  private String fuente;
  private String origen;
  private LocalDate fechaCarga;
}
