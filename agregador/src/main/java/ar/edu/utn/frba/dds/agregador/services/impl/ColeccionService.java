package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.HechoFuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.*;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {
  @Autowired
  private IColeccionRepository coleccionRepository;
  @Autowired
  private IHechoRepository hechoRepository;
  @Autowired
  private IFuenteRepository fuenteRepository;

  private final IHechoService hechoService;
  private LocalDateTime ultimaFechaRefresco;

  public ColeccionService(HechoService hechoService) {
    this.hechoService = hechoService;
    this.ultimaFechaRefresco = LocalDateTime.now().minusDays(1); // TODO: Debería persistirse
  }

  public Page<ColeccionOutputDTO> buscarColecciones(Pageable pageable) {
    Page<Coleccion> colecciones = this.coleccionRepository.findAll(pageable);
    List <Hecho> hechosNuevos = this.hechoService
            .actualizarHechos(colecciones
                    .stream()
                    .flatMap(coleccion -> coleccion
                            .getFuentes()
                            .stream()
                            .filter(f -> f.getTipoFuente().equals(TipoFuente.PROXY))
                            )
                    .toList());
    colecciones.forEach(c -> c.cargarHechos(hechosNuevos));
    // TODO: Buscar colecciones del servicio PROXY
    return colecciones.map(ColeccionOutputDTO::coleccionToDTO);
  }

  public List<HechoOutputDTO> buscarHechosColeccion(Long id, QueryParamsFiltro params) {
    Coleccion coleccion = this.findColecccionAux(id);
    List<Hecho> hechosProxy = this.pedirHechosProxy(coleccion.getFuentes()
        .stream()
        .filter(f -> f.getTipoFuente().equals(TipoFuente.PROXY))
        .collect(Collectors.toList()));
    coleccion.cargarHechos(hechosProxy);
    List<Hecho> hechosOutput = coleccion.consultarHechos(params.instanciarFiltros());
    return HechoOutputDTO.mapHechoToDTO(hechosOutput);
  }

  public List<HechoOutputDTO> buscarHechosCuradosColeccion(Long id, QueryParamsFiltro params) {
    Coleccion coleccion = this.findColecccionAux(id);
    List<Hecho> hechosProxy = this.pedirHechosProxy(coleccion.getFuentes().stream().filter(f -> f.getTipoFuente().equals(TipoFuente.PROXY)).toList());
    coleccion.cargarHechos(hechosProxy);
    List<Hecho> hechosOutput = coleccion.consultarHechosCurados(params.instanciarFiltros());
    return HechoOutputDTO.mapHechoToDTO(hechosOutput);
  }

  public ColeccionOutputDTO buscarColeccion(Long id) {
    Coleccion coleccion = this.findColecccionAux(id);
    List<Hecho> hechosProxy = this.pedirHechosProxy(coleccion.getFuentes().stream().filter(f -> f.getTipoFuente().equals(TipoFuente.PROXY)).toList());
    coleccion.cargarHechos(hechosProxy);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  // Guarda un Hecho en las colecciones
  public List<String> incorporarHecho(Hecho hecho) {
    List<Coleccion> colecciones = this.coleccionRepository.findAll();
    List<String> nombreColecciones = this.agregarHechoAColecciones(colecciones, hecho);
    boolean resultado = this.guardarColecciones(colecciones);
    if(!resultado){
      throw new RuntimeException("No se puedieron guardar las colecciones"); //TODO una excepción personalizar
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
      Fuente temp = this.fuenteRepository.findByNombre(fuente.getNombre());
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

    Coleccion coleccion = this.findColecccionAux(id);

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

    List<Fuente> fuentes = coleccion.getFuentes();
    List<Hecho> hechosFuentes = this.hechoRepository.findByFuentes(fuentes);
    coleccion.cargarHechos(hechosFuentes);
    coleccion.recalcularHechos();
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO agregarFuenteAColeccion(Long id, NombreFuenteInputDTO fuenteInputDTO) {
    Coleccion coleccion = this.findColecccionAux(id);

    Fuente fuente = this.fuenteRepository.findByNombre(fuenteInputDTO.getNombre());
    if(fuente == null){
      throw new RuntimeException("La fuente " + fuenteInputDTO + " no existe");
    }

    coleccion.agregarFuente(fuente);
    List<Fuente> fuentes = new ArrayList<>();
    fuentes.add(fuente);
    List<Hecho> hechos = this.hechoRepository.findByFuentes(fuentes);
    coleccion.cargarHechos(hechos);

    this.coleccionRepository.save(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO quitarFuentesAColeccion(Long id, List<NombreFuenteInputDTO> fuentesInputDTO) {
    Coleccion coleccion = this.findColecccionAux(id);

    List<Fuente> fuentesColeccion = new ArrayList<>();
    fuentesInputDTO.forEach(fuente -> {
      Fuente temp = this.fuenteRepository.findByNombre(fuente.getNombre());
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
  public void eliminarColeccion(Long id) { // TODO: Hacer baja lógica
      coleccionRepository.deleteById(id);
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
    List<Fuente> fuentesHechos = hechos.stream()
            .map(h -> h.getFuenteSet()
                    .stream()
                    .map(HechoFuente::getFuente)
                    .toList())
            .flatMap(Collection::stream)
            .toList();

    hechos.forEach(hecho -> {
      colecciones.forEach(c -> {
        if(new HashSet<>(c.getFuentes()).containsAll(fuentesHechos)) {
          if(c.getHechos()==null || c.getHechos().stream().noneMatch(h -> h.getId().equals(hecho.getId()))) { // TODO: Revisar, esto es lo que hace que no haya hechos repetidos en las colecciones
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

  private List<Hecho> pedirHechosProxy(List<Fuente> fuentes) {
    return fuentes.stream()
        .map(fuente -> fuente.importarHechos().toStream().toList())
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
