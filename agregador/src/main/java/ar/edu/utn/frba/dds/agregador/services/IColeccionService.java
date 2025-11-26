package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.*;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.CriterioInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IColeccionService {
  // Busqueda de colecciones - hechos
  public Page<ColeccionOutputDTO> buscarColecciones(Pageable pageable);
  public Page<HechoOutputDTO> buscarHechosColeccion(Long id, QueryParamsFiltro params, Pageable pageable);
  public List<HechoOutputDTO> buscarHechosCuradosColeccion(Long id, QueryParamsFiltro params);
  public Page<HechoOutputDTO> buscarHechosCuradosColeccionPaginado(Long id, QueryParamsFiltro params, Pageable pageable);
  public ColeccionOutputDTO buscarColeccion(Long id);
  public ColeccionOutputDTO buscarInfoColeccion(Long id);

  // Operaciones sobre colecciones
  public List<String> incorporarHecho(Hecho hecho);
  public Boolean eliminarHechoDeColecciones(Hecho hecho);
  public void incorporarHechos(List<Hecho> hecho);
  public void eliminarColeccion(Long id);

  // Modificar colecciones
  public ColeccionOutputDTO guardarColeccion(ColeccionInputDTO coleccion);
  public ColeccionOutputDTO agregarFiltrosCriterio(Long id, CriterioInputDTO criterioInputDTO);
  public ColeccionOutputDTO quitarFiltrosCriterio(Long id, CriterioInputDTO criterio);
  public ColeccionOutputDTO quitarFuentesAColeccion(Long id, List<NombreFuenteInputDTO> fuentesInputDTO);
  public ColeccionOutputDTO agregarFuenteAColeccion(Long id, NombreFuenteInputDTO fuenteInputDTO);
  public ColeccionOutputDTO actualizarColeccion(Long id, ColeccionInputDTO coleccion);
  public ColeccionOutputDTO agregarConsensoAColeccion(Long id, Consenso consenso);
  List<HechoOutputDTO> buscarHechosPorConsensos(Long idColeccion, List<Consenso> consensos);
}
