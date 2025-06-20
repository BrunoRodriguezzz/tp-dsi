package ar.edu.utn.frba.dds.agregador.models.dtos.external;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.UbicacionInputDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class HechoServicioResponseDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionInputDTO ubicacion;
  private LocalDate fechaAcontecimiento;
  private ContribuyenteServicioResponseDTO contribuyente;
  private String fuente;
  private String origen;
  private List<String> contenidoMultimedia;
  private List<String> etiquetas;
}
