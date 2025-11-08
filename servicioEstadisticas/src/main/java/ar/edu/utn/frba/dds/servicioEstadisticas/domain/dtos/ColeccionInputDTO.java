package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Coleccion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ColeccionInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  @JsonProperty(value = "hechos")
  private List<HechoInputDTO> hechos;
  private long cantidadHechos;
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
