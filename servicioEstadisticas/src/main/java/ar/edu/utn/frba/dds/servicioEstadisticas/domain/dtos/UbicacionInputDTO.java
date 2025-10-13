package ar.edu.utn.frba.dds.servicioAutenticacion.domain.dtos;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Provincia;
import lombok.Data;

@Data
public class UbicacionInputDTO {
  private String latitud;
  private String longitud;
  private String Pais;
  private Provincia provincia;
  private String muncipio;
}
