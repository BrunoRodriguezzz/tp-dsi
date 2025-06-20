package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import java.util.List;

public interface IAgregadorService {
  public List<HechoOutputDTO> buscarHechos();
  public List<String> incorporarHecho(HechoInputDTO hecho);
  public void refrescarColecciones();
}
