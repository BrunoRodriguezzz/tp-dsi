package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.List;

public interface IColeccionService {
  public List<ColeccionOutputDTO> buscarColecciones();
  public List<HechoOutputDTO> buscarHechosColeccion(Long id, QueryParamsFiltro params);
  public ColeccionOutputDTO buscarColeccion(Long id);
  public List<String> incorporarHecho(Hecho hecho);
  public Boolean eliminarHechoDeColecciones(Hecho hecho);
  public void incorporarHechos(List<Hecho> hecho);

  public ColeccionOutputDTO guardarColeccion(ColeccionInputDTO coleccion);
  public ColeccionOutputDTO actualizarColeccion(Long id, ColeccionInputDTO coleccion);
  public void eliminarColeccion(Long id);
}
