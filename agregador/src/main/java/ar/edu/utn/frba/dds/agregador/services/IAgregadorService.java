package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import java.util.List;

public interface IAgregadorService {
  public List<String> incorporarHecho(HechoInputDTO hecho);
}
