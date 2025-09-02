package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import lombok.Data;

@Data
public class FuenteOutputDTO {
  private String nombre;
  private String path;
  private String tipoFuente;
  private Long   idInterno;
}
