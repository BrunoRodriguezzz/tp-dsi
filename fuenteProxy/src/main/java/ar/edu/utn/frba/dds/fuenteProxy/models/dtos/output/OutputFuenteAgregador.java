package ar.edu.utn.frba.dds.fuenteProxy.models.dtos.output;

import lombok.Data;

@Data
public class OutputFuenteAgregador {
  private String nombre;
  private String path;
  private String tipoFuente;
  private Long idInterno;
}
