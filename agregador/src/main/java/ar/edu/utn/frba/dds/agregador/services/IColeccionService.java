package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.List;

public interface IColeccionService {
  public List<ColeccionOutputDTO> buscarColecciones();
  public ColeccionOutputDTO buscarColeccion(Long id);
  public List<String> incorporarHecho(Hecho hecho);
  public Boolean eliminarHechoDeColecciones(Hecho hecho);
  public void incorporarHechos(List<Hecho> hecho);
}
