package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import java.util.List;

public interface IHechoService {
  public List<HechoOutputDTO> buscarHechos();
  public Hecho incorporarHecho(HechoInputDTO hecho);
  public Hecho guardarHecho(Hecho hecho);
  public Hecho buscarHecho(Long id);
  public List<Hecho> guardarHechos(List<Hecho> hechos);
}
