package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import lombok.Data;

@Data
public class UbicacionInputDTO {
  private String latitud;
  private String longitud;
  private String pais;
  private String provincia;
  private String municipio;
}
