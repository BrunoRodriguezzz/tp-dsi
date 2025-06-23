package ar.edu.utn.frba.dds.agregador.exceptions.exceptions;

import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import lombok.Data;

@Data
public class HechoYaExistenteException extends RuntimeException {
  private HechoOutputDTO hechoExistente;

  public HechoYaExistenteException(String mensaje, Hecho hecho) {
    super(mensaje);
    this.hechoExistente = UtilsDTO.HechoToDTO(hecho);
  }
}
