package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import java.util.List;

public interface IAgregadorService {
  public List<String> incorporarHecho(HechoInputDTO hecho);
  public Fuente incorporarFuente(FuenteInputDTO fuente);
  public void eliminarHecho(Long id);
}
