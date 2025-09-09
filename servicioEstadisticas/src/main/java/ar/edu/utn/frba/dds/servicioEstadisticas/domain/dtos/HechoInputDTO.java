package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Hecho;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HechoInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private UbicacionInputDTO ubicacion;
  private LocalDateTime fechaAcontecimiento;
  private ContribuyenteInputDTO contribuyente;
  private String fuente;
  private String origen;
  private LocalDateTime fechaCarga;

  public Categoria convertirACategoria() {
    if (this.categoria == null) return null;
    Categoria categoria = new Categoria();
    categoria.setDetalle(this.categoria);
    return categoria;
  }

  public Hecho convertirAHecho() {
    return new Hecho(this.id);
  }
}
