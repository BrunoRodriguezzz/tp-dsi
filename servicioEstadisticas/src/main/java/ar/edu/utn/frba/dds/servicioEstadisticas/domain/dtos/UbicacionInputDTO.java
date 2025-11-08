package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Provincia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class UbicacionInputDTO {
  private String latitud;
  private String longitud;
  private String Pais;
  private Provincia provincia;
  private String muncipio;
}
