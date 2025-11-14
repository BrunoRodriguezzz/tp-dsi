package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Provincia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class UbicacionInputDTO {
  private Double latitud;
  private Double longitud;
  private String pais;
  private Provincia provincia;
  private String muncipio;
}
