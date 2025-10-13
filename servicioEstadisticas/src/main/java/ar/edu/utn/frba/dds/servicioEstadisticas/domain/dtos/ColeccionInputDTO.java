package ar.edu.utn.frba.dds.servicioAutenticacion.domain.dtos;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Coleccion;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private List<HechoInputDTO> hechos;
  private CriterioInputDTO criterio;
  private List<String> fuentes;
  private List<String> consensos;

  public Coleccion convertirAColeccion() {
    Coleccion coleccion = new Coleccion();
    coleccion.setId(this.id);
    coleccion.setDetalle(this.titulo);
    return coleccion;
  }
}
