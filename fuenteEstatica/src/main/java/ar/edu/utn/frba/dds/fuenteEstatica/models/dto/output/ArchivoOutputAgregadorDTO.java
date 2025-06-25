package ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output;

import lombok.Data;

@Data
public class ArchivoOutputAgregadorDTO {
  private String nombre;
  private String path;
  private String tipoArchivo;
  private Long idInterno;
}
