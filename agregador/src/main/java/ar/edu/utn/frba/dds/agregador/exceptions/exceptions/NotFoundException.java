package ar.edu.utn.frba.dds.agregador.exceptions.exceptions;

import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {
  private HechoOutputDTO hechoExistente;

  public NotFoundException(String mensaje) {
    super(mensaje);
  }
}
