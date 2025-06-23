package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.UtilsDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {
  @Autowired
  private IColeccionRepository coleccionRepository;

  private IFuenteService fuenteService;
  private IHechoService hechoService;

  public ColeccionService(IFuenteService fuenteService, HechoService hechoService) {
    this.hechoService = hechoService;
    this.fuenteService = fuenteService;
  }

  public List<ColeccionOutputDTO> buscarColecciones() {
      List <Coleccion> colecciones = this.coleccionRepository.buscarCopiaColecciones();
//    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
//    List<Hecho> hechosGuardados = this.hechoService.guardarHechos(hechosProxy);
//    colecciones.forEach(coleccion -> {
//      coleccion.cargarHechos(hechosGuardados);
//    });
    List <ColeccionOutputDTO> coleccionesDTO = UtilsDTO.mapColeccionesToDTO(colecciones);
    return coleccionesDTO;
  }

  public List<HechoOutputDTO> buscarHechosColeccion(Long id, QueryParamsFiltro params) {
    Coleccion coleccion = this.coleccionRepository.buscarCopiaColeccion(id);
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    coleccion.cargarHechos(hechosProxy);
    List<Hecho> hechosOutput = coleccion.consultarHechos(params.instanciarFiltros());
    List<HechoOutputDTO> hechosOutputDTO = UtilsDTO.mapHechoToDTO(hechosOutput);
    return hechosOutputDTO;
  }

  public ColeccionOutputDTO buscarColeccion(Long id) {
    Coleccion coleccion = this.coleccionRepository.buscarCopiaColeccion(id);
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    coleccion.cargarHechos(hechosProxy);
    ColeccionOutputDTO coleccionDTO = UtilsDTO.coleccionToDTO(coleccion);
    return coleccionDTO;
  }

  // Guarda un Hecho en las colecciones
  public List<String> incorporarHecho(Hecho hecho) {
    List<Coleccion> colecciones = this.coleccionRepository.buscarColecciones();
    List<String> nombreColecciones = this.agregarHechoAColecciones(colecciones, hecho);
    Boolean resultado = this.coleccionRepository.guardarColecciones(colecciones);
    if(!resultado){
      throw new RuntimeException("No se puedieron guardar las colecciones");
    }
    return nombreColecciones;
  }

  // Guarda multiples Hechos en las colecciones
  public void incorporarHechos(List<Hecho> hechos) {
    List<Coleccion> colecciones = this.coleccionRepository.buscarColecciones();
    this.agregarHechosAColecciones(hechos, colecciones);
    Boolean resultado = this.coleccionRepository.guardarColecciones(colecciones);
    if(!resultado){
      throw new RuntimeException("No se puedieron guardar las colecciones");
    }
  }

  public Boolean eliminarHechoDeColecciones(Hecho hecho) {
    return this.coleccionRepository.eliminarHechoDeColecciones(hecho);
  }

  @Override
  public ColeccionOutputDTO guardarColeccion(ColeccionInputDTO coleccionInputDTO) {
    Coleccion coleccion = UtilsDTO.inputColeccionToColeccion(coleccionInputDTO);
    // TODO: Funcion que le cargue los hechos
    this.coleccionRepository.guardarColeccion(coleccion);
    return UtilsDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO actualizarColeccion(Long id, ColeccionInputDTO coleccionInputDTO) {
    Coleccion coleccion = coleccionRepository.buscarColeccion(id);
    if(coleccion == null) {
      throw new NotFoundException("No se encontro la coleccion");
    }
    this.actualizarDatosColeccion(coleccion, coleccionInputDTO);
    this.coleccionRepository.guardarColeccion(coleccion);
    return UtilsDTO.coleccionToDTO(coleccion);
  }

  @Override
  public void eliminarColeccion(Long id) {
      coleccionRepository.eliminarColeccion(id);
  }

    // ---------------------------------------------------- Privados ----------------------------------------------------
  private List<String> agregarHechoAColecciones(List<Coleccion> colecciones, Hecho hecho) {
    List<String> nombreColecciones = new ArrayList<>();
    colecciones.forEach(coleccion -> {
        boolean resultado = coleccion.cargarHecho(hecho);
        if (resultado) {
          nombreColecciones.add(coleccion.getTitulo());
      }
    });
    return nombreColecciones;
  }

  private void agregarHechosAColecciones(List<Hecho> hechos, List<Coleccion> colecciones) {
    // TODO Es poco eficiciente pero funciona
    hechos.forEach(hecho -> {
      colecciones.forEach(c -> {
        if(c.getFuentes().contains(hecho.getFuente())) {
          if(c.getHechos()==null || c.getHechos().stream().noneMatch(h -> h.equals(hecho))) {
            c.cargarHecho(hecho);
            this.hechoService.guardarHecho(hecho);
          }
        }
      });
    });
  }

  private void actualizarDatosColeccion(Coleccion coleccion, ColeccionInputDTO coleccionInputDTO) {
      if (coleccionInputDTO.getNombre() != null) {
          coleccion.setTitulo(coleccionInputDTO.getNombre());
      }

      if (coleccionInputDTO.getDescripcion() != null) {
          coleccion.setDescripcion(coleccionInputDTO.getDescripcion());
      }

      List<Filtro> filtros = UtilsDTO.crearFiltros(coleccionInputDTO);
      if (!filtros.isEmpty()) {
        coleccion.getCriterio().setFiltros(filtros);
      }
  }
}
