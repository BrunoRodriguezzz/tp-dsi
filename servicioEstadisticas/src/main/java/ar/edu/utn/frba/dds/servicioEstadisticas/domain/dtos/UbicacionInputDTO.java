package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Provincia;
import lombok.Data;

@Data
public class UbicacionInputDTO {
  private String latitud;
  private String longitud;
  private Provincia provincia;
}
