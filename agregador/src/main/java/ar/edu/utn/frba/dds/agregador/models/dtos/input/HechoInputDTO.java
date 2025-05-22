package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;
import java.time.LocalDate;
import lombok.Data;

@Data
public class HechoInputDTO {
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionDTO ubicacion;
  private LocalDate fechaAcontecimiento;
  private String origen;
}
