package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.*;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.CriterioInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;

public interface IColeccionService {
  public List<ColeccionOutputDTO> buscarColecciones();
  public List<HechoOutputDTO> buscarHechosColeccion(Long id, QueryParamsFiltro params);
  public List<HechoOutputDTO> buscarHechosCuradosColeccion(Long id, QueryParamsFiltro params);
  public ColeccionOutputDTO buscarColeccion(Long id);
  public List<String> incorporarHecho(Hecho hecho);
  public Boolean eliminarHechoDeColecciones(Hecho hecho);
  public void incorporarHechos(List<Hecho> hecho);
  public void refrescarColecciones();
  public ColeccionOutputDTO guardarColeccion(ColeccionInputDTO coleccion);
  public ColeccionOutputDTO agregarFiltrosCriterio(Long id, CriterioInputDTO criterioInputDTO);
  public ColeccionOutputDTO quitarFiltrosCriterio(Long id, CriterioInputDTO criterio);
  public ColeccionOutputDTO quitarFuentesAColeccion(Long id, List<NombreFuenteInputDTO> fuentesInputDTO);
  public ColeccionOutputDTO agregarFuenteAColeccion(Long id, NombreFuenteInputDTO fuenteInputDTO);
  public ColeccionOutputDTO actualizarColeccion(Long id, ColeccionInputDTO coleccion);
  public ColeccionOutputDTO agregarConsensoAColeccion(Long id, Consenso consenso);
  public void eliminarColeccion(Long id);
}
