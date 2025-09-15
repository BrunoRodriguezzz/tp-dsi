package ar.edu.utn.frba.dds.fuenteProxy.Services;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputFuenteDTO;

public interface IFuenteService {
  public void guardarFuenteInput(InputFuenteDTO inputFuenteDTO);
  public void guardarFuente(Fuente fuente);
  // TODO: Ir completando.
}
