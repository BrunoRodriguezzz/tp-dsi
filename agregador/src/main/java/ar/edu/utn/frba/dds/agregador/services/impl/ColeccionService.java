package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.CriterioInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {
  @Autowired
  private IColeccionRepository coleccionRepository;

  private IFuenteService fuenteService;
  private IHechoService hechoService;
  private LocalDateTime ultimaFechaRefresco;

  public ColeccionService(IFuenteService fuenteService, HechoService hechoService) {
    this.hechoService = hechoService;
    this.fuenteService = fuenteService;
    this.ultimaFechaRefresco = LocalDateTime.now().minusDays(1); // TODO: Debería persistirse
  }

  public List<ColeccionOutputDTO> buscarColecciones() {
    List <Coleccion> colecciones = this.coleccionRepository.buscarCopiaColecciones();
    // TODO: Pedirle a la proxy solo los hechos de las fuentes que use. No todos, evitar consultas al pedo
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    List<Hecho> hechosGuardados = this.hechoService.guardarHechos(hechosProxy);
    colecciones.forEach(coleccion -> {
      coleccion.cargarHechos(hechosGuardados);
    });
    // TODO: Buscar colecciones del servicio PROXY
    List <ColeccionOutputDTO> coleccionesDTO = ColeccionOutputDTO.mapColeccionesToDTO(colecciones);
    return coleccionesDTO;
  }

  public List<HechoOutputDTO> buscarHechosColeccion(Long id, QueryParamsFiltro params) {
    Coleccion coleccion = this.coleccionRepository.buscarCopiaColeccion(id);
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    coleccion.cargarHechos(hechosProxy);
    List<Hecho> hechosOutput = coleccion.consultarHechos(params.instanciarFiltros());
    List<HechoOutputDTO> hechosOutputDTO = HechoOutputDTO.mapHechoToDTO(hechosOutput);
    return hechosOutputDTO;
  }

  public List<HechoOutputDTO> buscarHechosCuradosColeccion(Long id, QueryParamsFiltro params) {
    Coleccion coleccion = this.coleccionRepository.buscarCopiaColeccion(id);
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    coleccion.cargarHechos(hechosProxy);
    List<Hecho> hechosOutput = coleccion.consultarHechosCurados(params.instanciarFiltros());
    List<HechoOutputDTO> hechosOutputDTO = HechoOutputDTO.mapHechoToDTO(hechosOutput);
    return hechosOutputDTO;
  }

  public ColeccionOutputDTO buscarColeccion(Long id) {
    Coleccion coleccion = this.coleccionRepository.buscarCopiaColeccion(id);
    // TODO: Pedirle a la proxy solo los hechos de las fuentes que use. No todos, evitar consultas al pedo
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    coleccion.cargarHechos(hechosProxy);
    ColeccionOutputDTO coleccionDTO = ColeccionOutputDTO.coleccionToDTO(coleccion);
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
    List<Fuente> fuentesColeccion = new ArrayList<>();
    coleccionInputDTO.getFuentes().forEach(fuente -> {
      Fuente temp = this.fuenteService.buscarFuente(fuente.getNombre());
      if(temp == null){
        throw new RuntimeException("La fuente " + fuente.getNombre() + " no existe");
      }
      fuentesColeccion.add(temp);
    });
    Coleccion coleccion = ColeccionInputDTO.inputColeccionToColeccion(coleccionInputDTO, fuentesColeccion);
    this.coleccionRepository.guardarColeccion(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO agregarFiltrosCriterio(Long id, CriterioInputDTO criterioInputDTO) {
    List<Filtro> nuevosFiltros = CriterioInputDTO.crearFiltros(criterioInputDTO);
    Criterio criterio = new Criterio(nuevosFiltros);

    Coleccion coleccion = coleccionRepository.buscarColeccion(id);
    if(coleccion == null) {
      throw new NotFoundException("No se encontro la coleccion");
    }

    coleccion.setCriterio(criterio);
    coleccion.recalcularHechos();

    this.coleccionRepository.guardarColeccion(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO quitarFiltrosCriterio(Long id, CriterioInputDTO criterio) {
    Coleccion coleccion = coleccionRepository.buscarColeccion(id);
    if(coleccion == null) {
      throw new NotFoundException("No se encontro la coleccion");
    }

    List<Filtro> filtrosNuevos = CriterioInputDTO.crearFiltros(criterio);
    coleccion.cambiarCriterio(new Criterio(filtrosNuevos));

    List<Hecho> hechosFuentes = coleccion.getFuentes()
        .stream()
        .map(f -> {
          return this.hechoService.buscarHechosGuardadosFuente(f);
        })
        .flatMap(List::stream)
        .collect(Collectors.toList());
    coleccion.cargarHechos(hechosFuentes);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO agregarFuenteAColeccion(Long id, FuenteInputDTO fuenteInputDTO) {
    Coleccion coleccion = coleccionRepository.buscarColeccion(id);
    if(coleccion == null) {
      throw new NotFoundException("No se encontro la coleccion");
    }

    Fuente fuente = this.fuenteService.buscarFuente(fuenteInputDTO.getNombre());
    if(fuente == null){
      throw new RuntimeException("La fuente " + fuenteInputDTO.getNombre() + " no existe");
    }

    coleccion.agregarFuente(fuente);
    List<Hecho> hechos = fuente.importarHechos();
    hechoService.guardarHechos(hechos);
    coleccion.cargarHechos(hechos);

    this.coleccionRepository.guardarColeccion(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO quitarFuentesAColeccion(Long id, List<FuenteInputDTO> fuentesInputDTO) {
    Coleccion coleccion = coleccionRepository.buscarColeccion(id);
    if(coleccion == null) {
      throw new NotFoundException("No se encontro la coleccion");
    }

    List<Fuente> fuentesColeccion = new ArrayList<>();
    fuentesInputDTO.forEach(fuente -> {
      Fuente temp = this.fuenteService.buscarFuente(fuente.getNombre());
      if(temp == null){
        throw new RuntimeException("La fuente " + fuente.getNombre() + " no existe");
      }
      fuentesColeccion.add(temp);
    });

    coleccion.setFuentes(fuentesColeccion);
    coleccion.recalcularHechos();

    this.coleccionRepository.guardarColeccion(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO agregarConsensoAColeccion(Long id, Consenso consenso) {
    Coleccion coleccion = coleccionRepository.buscarColeccion(id);
    if(coleccion == null) {
      throw new NotFoundException("No se encontro la coleccion");
    }

    coleccion.agregarConsenso(consenso);

    this.coleccionRepository.guardarColeccion(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO actualizarColeccion(Long id, ColeccionInputDTO coleccionInputDTO) {
    Coleccion coleccion = coleccionRepository.buscarColeccion(id);
    if(coleccion == null) {
      throw new NotFoundException("No se encontro la coleccion");
    }
    this.actualizarDatosColeccion(coleccion, coleccionInputDTO);
    this.coleccionRepository.guardarColeccion(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
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
          if(c.getHechos()==null || c.getHechos().stream().noneMatch(h -> h.equals(hecho))) { // TODO: Revisar, esto es lo que hace que no haya hechos repetidos en las colecciones
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
  }

  @Override
  public void refrescarColecciones(){
    List<Hecho> nuevosHechos = this.fuenteService.buscarNuevosHechos(this.ultimaFechaRefresco);
    List<Hecho> hechos = this.hechoService.guardarHechos(nuevosHechos);
    this.incorporarHechos(hechos);
    this.ultimaFechaRefresco = LocalDateTime.now();
  }
}
