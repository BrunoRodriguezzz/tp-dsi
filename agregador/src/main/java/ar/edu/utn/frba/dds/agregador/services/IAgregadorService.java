package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.List;

public interface IAgregadorService {
  public List<Hecho> buscarHechos();
  public List<String> incorporarHecho(HechoInputDTO hecho);
  public Hecho DTOtoHecho(HechoInputDTO hechoDTO);
}
