package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.*;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {
  @Autowired
  private IColeccionRepository coleccionRepository;

  private final IFuenteService fuenteService;
  private final IHechoService hechoService;
  private LocalDateTime ultimaFechaRefresco;

  public ColeccionService(IFuenteService fuenteService, HechoService hechoService) {
    this.hechoService = hechoService;
    this.fuenteService = fuenteService;
    this.ultimaFechaRefresco = LocalDateTime.now().minusDays(1); // TODO: Debería persistirse
  }

  public List<ColeccionOutputDTO> buscarColecciones() {
    List <Coleccion> colecciones = this.coleccionRepository.findAll();
    // TODO: Pedirle a la proxy solo los hechos de las fuentes que use. No todos, evitar consultas al pedo
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    List<Hecho> hechosGuardados = this.hechoService.guardarHechos(hechosProxy);
    colecciones.forEach(coleccion -> {
      coleccion.cargarHechos(hechosGuardados);
    });
    // TODO: Buscar colecciones del servicio PROXY
    return ColeccionOutputDTO.mapColeccionesToDTO(colecciones);
  }

  public List<HechoOutputDTO> buscarHechosColeccion(Long id, QueryParamsFiltro params) {
    Coleccion coleccion = this.findColecccionAux(id);
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    coleccion.cargarHechos(hechosProxy);
    List<Hecho> hechosOutput = coleccion.consultarHechos(params.instanciarFiltros());
    return HechoOutputDTO.mapHechoToDTO(hechosOutput);
  }

  public List<HechoOutputDTO> buscarHechosCuradosColeccion(Long id, QueryParamsFiltro params) {
    Coleccion coleccion = this.findColecccionAux(id);
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    coleccion.cargarHechos(hechosProxy);
    List<Hecho> hechosOutput = coleccion.consultarHechosCurados(params.instanciarFiltros());
      return HechoOutputDTO.mapHechoToDTO(hechosOutput);
  }

  public ColeccionOutputDTO buscarColeccion(Long id) {
    Coleccion coleccion = this.findColecccionAux(id);
    // TODO: Pedirle a la proxy solo los hechos de las fuentes que use. No todos, evitar consultas al pedo
    List<Hecho> hechosProxy = this.fuenteService.buscarHechosFuente(TipoFuente.PROXY);
    coleccion.cargarHechos(hechosProxy);
      return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  // Guarda un Hecho en las colecciones
  public List<String> incorporarHecho(Hecho hecho) {
    List<Coleccion> colecciones = this.coleccionRepository.findAll();
    List<String> nombreColecciones = this.agregarHechoAColecciones(colecciones, hecho);
    boolean resultado = this.guardarColecciones(colecciones);
    if(!resultado){
      throw new RuntimeException("No se puedieron guardar las colecciones");
    }
    return nombreColecciones;
  }

  // Guarda multiples Hechos en las colecciones
  public void incorporarHechos(List<Hecho> hechos) {
    List<Coleccion> colecciones = this.coleccionRepository.findAll();
    this.agregarHechosAColecciones(hechos, colecciones);
    Boolean resultado = this.guardarColecciones(colecciones);
    if(!resultado){
      throw new RuntimeException("No se puedieron guardar las colecciones");
    }
  }

  public Boolean eliminarHechoDeColecciones(Hecho hecho) {
    return this.deleteHechoFromColeccion(hecho);
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
    this.coleccionRepository.save(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO agregarFiltrosCriterio(Long id, CriterioInputDTO criterioInputDTO) {
    List<Filtro> nuevosFiltros = CriterioInputDTO.crearFiltros(criterioInputDTO);
    //Criterio criterio = new Criterio(nuevosFiltros);

    Coleccion coleccion = this.findColecccionAux(id);

    //coleccion.setCriterio(criterio);
    coleccion.getCriterio().getFiltros().addAll(nuevosFiltros);
    coleccion.recalcularHechos();

    this.coleccionRepository.save(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO quitarFiltrosCriterio(Long id, CriterioInputDTO criterio) {
    Coleccion coleccion = this.findColecccionAux(id);

    List<Filtro> filtrosNuevos = CriterioInputDTO.crearFiltros(criterio);
    coleccion.cambiarCriterio(new Criterio(filtrosNuevos));

    List<Hecho> hechosFuentes = coleccion.getFuentes()
        .stream()
        .map(f -> this.hechoService.buscarHechosGuardadosFuente(f))
        .flatMap(List::stream)
        .collect(Collectors.toList());
    coleccion.cargarHechos(hechosFuentes);
    coleccion.recalcularHechos();
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO agregarFuenteAColeccion(Long id, NombreFuenteInputDTO fuenteInputDTO) {
    Coleccion coleccion = this.findColecccionAux(id);

    Fuente fuente = this.fuenteService.buscarFuente(fuenteInputDTO.getNombre());
    if(fuente == null){
      throw new RuntimeException("La fuente " + fuenteInputDTO + " no existe");
    }

    coleccion.agregarFuente(fuente);
    List<Hecho> hechos = this.fuenteService.buscarHechosFuente(fuenteInputDTO.getNombre());
    hechoService.guardarHechos(hechos);
    coleccion.cargarHechos(hechos);

    this.coleccionRepository.save(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO quitarFuentesAColeccion(Long id, List<NombreFuenteInputDTO> fuentesInputDTO) {
    Coleccion coleccion = this.findColecccionAux(id);

    List<Fuente> fuentesColeccion = new ArrayList<>();
    fuentesInputDTO.forEach(fuente -> {
      Fuente temp = this.fuenteService.buscarFuente(fuente.getNombre());
      if(temp == null){
        throw new RuntimeException("La fuente " + fuente + " no existe");
      }
      fuentesColeccion.add(temp);
    });

    //coleccion.setFuentes(fuentesColeccion);
    coleccion.getFuentes().removeAll(fuentesColeccion);
    coleccion.recalcularHechos();

    this.coleccionRepository.save(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO agregarConsensoAColeccion(Long id, Consenso consenso) {
    Coleccion coleccion = this.findColecccionAux(id);
    coleccion.agregarConsenso(consenso);

    this.coleccionRepository.save(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO actualizarColeccion(Long id, ColeccionInputDTO coleccionInputDTO) {
    Coleccion coleccion = this.findColecccionAux(id);

    this.actualizarDatosColeccion(coleccion, coleccionInputDTO);
    this.coleccionRepository.save(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public void eliminarColeccion(Long id) {
      coleccionRepository.deleteById(id);
  }

  @Override
  public void refrescarColecciones(){
    List<Hecho> nuevosHechos = this.fuenteService.buscarNuevosHechos(this.ultimaFechaRefresco);
    List<Hecho> hechos = this.hechoService.guardarHechos(nuevosHechos);
    this.incorporarHechos(hechos);
    this.ultimaFechaRefresco = LocalDateTime.now();
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

  private Coleccion findColecccionAux(Long id) {
    Optional<Coleccion> coleccion = this.coleccionRepository.findById(id);
    if (coleccion.isPresent()) {
      return coleccion.get();
    }
    else {
      throw new NotFoundException("Coleccion no encontrada");
    }
  }

  private Boolean guardarColecciones(List<Coleccion> colecciones) {
    List<Coleccion> aux = this.coleccionRepository.saveAll(colecciones);
    return aux.size() == colecciones.size();
  }

  private Boolean deleteHechoFromColeccion(Hecho hecho) {
    List<Hecho> hechos = new ArrayList<>();
    hechos.add(hecho);
    List<Coleccion> colecciones = this.coleccionRepository.findColeccionsByHechos(hechos); // POSIBLE CACA

    AtomicBoolean resultado = new AtomicBoolean(false);

    colecciones.forEach(coleccion -> {
      List<Hecho> aux = coleccion.getHechos();
      boolean borrado = aux.remove(hecho);
      resultado.set(resultado.get() || borrado); // true si al menos uno fue eliminado
      coleccion.setHechos(aux);
      coleccionRepository.save(coleccion);
    });

    return resultado.get();
  }
}
