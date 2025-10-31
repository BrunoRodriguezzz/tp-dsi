package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.EntidadFiltro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.FiltroMapper;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ColeccionService implements IColeccionService {
  @Autowired
  private IColeccionRepository coleccionRepository;
  @Autowired
  private IHechoRepository hechoRepository;
  @Autowired
  private IFuenteRepository fuenteRepository;
  @Autowired
  private FiltroMapper filtroMapper;

  @Autowired
  private ApplicationContext applicationContext;

  private final IHechoService hechoService;

  public ColeccionService(HechoService hechoService) {
    this.hechoService = hechoService;
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
    return colecciones.map(ColeccionOutputDTO::coleccionToDTO);
  }

  public Page<HechoOutputDTO> buscarHechosColeccion(Long id, QueryParamsFiltro params, Pageable pageable) {
    boolean tieneFiltros = (params.categoria != null && !params.categoria.isEmpty()) ||
            params.fechaAcontecimientoInicio != null || params.fechaAcontecimientoFin != null ||
            params.latitud != null || params.longitud != null ||
            (params.titulo != null && !params.titulo.isEmpty()) ||
            (params.fuente != null && !params.fuente.isEmpty()) ||
            params.fechaCargaInicio != null || params.fechaCargaFin != null;

    if (!tieneFiltros) {
      Page<ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho> hechosPaginados = this.hechoRepository.findByColeccionId(id, pageable);
      return hechosPaginados.map(ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO::HechoToDTO);
    }

    Coleccion coleccion = this.findColecccionAux(id);
    List<Hecho> hechosProxy = this.pedirHechosProxy(coleccion.getFuentes().stream().filter(f -> f.getTipoFuente().equals(TipoFuente.PROXY)).toList());
    coleccion.cargarHechos(hechosProxy);

    List<Hecho> hechosFiltrados = coleccion.consultarHechos(params.instanciarFiltros());

    int total = hechosFiltrados.size();
    int page = pageable.getPageNumber();
    int size = pageable.getPageSize();
    int fromIndex = Math.min(page * size, total);
    int toIndex = Math.min(fromIndex + size, total);
    List<Hecho> content = hechosFiltrados.subList(fromIndex, toIndex);

    org.springframework.data.domain.Page<ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho> hechosPage =
            new org.springframework.data.domain.PageImpl<>(content, pageable, total);

    return hechosPage.map(ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO::HechoToDTO);
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

  public List<String> incorporarHecho(Hecho hecho) {
    List<Coleccion> colecciones = this.coleccionRepository.findAll();
    List<String> nombreColecciones = this.agregarHechoAColecciones(colecciones, hecho);
    boolean resultado = this.guardarColecciones(colecciones);
    if(!resultado){
      throw new RuntimeException("No se puedieron guardar las colecciones"); //TODO una excepción personalizar
    }
    return nombreColecciones;
  }

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
    log.info("Entrando en guardarColeccion para: {}", coleccionInputDTO.getNombre());
    List<Fuente> fuentesColeccion = new ArrayList<>();
    coleccionInputDTO.getFuentes().forEach(fuente -> {
      Fuente temp = this.fuenteRepository.findByNombre(fuente.getNombre());
      if (temp == null) {
        throw new RuntimeException("La fuente " + fuente.getNombre() + " no existe");
      }
      fuentesColeccion.add(temp);
    });

    List<Filtro> filtrosTransitorios = CriterioInputDTO.crearFiltros(coleccionInputDTO.getCriterio());
    List<EntidadFiltro> filtrosGestionados = this.filtroMapper.toEntities(filtrosTransitorios);

    Criterio criterio = new Criterio();
    criterio.agregarFiltros(filtrosGestionados);

    Coleccion nuevaColeccion = new Coleccion(
            coleccionInputDTO.getNombre(),
            coleccionInputDTO.getDescripcion(),
            fuentesColeccion,
            criterio
    );

    log.info("Guardando colección (sin hechos) con título='{}'...", coleccionInputDTO.getNombre());
    Coleccion coleccionGuardada = this.coleccionRepository.save(nuevaColeccion);
    log.info("Colección persistida con id={} (título='{}'). Iniciando importación asíncrona de hechos...", coleccionGuardada.getId(), coleccionGuardada.getTitulo());

    applicationContext.getBean(ColeccionService.class).importarYAsociarHechos(coleccionGuardada.getId(), fuentesColeccion);

    log.info("Saliendo de guardarColeccion (se devuelve resultado sin esperar importación) para id={}", coleccionGuardada.getId());

    return ColeccionOutputDTO.coleccionToDTO(coleccionGuardada);
  }

  @Async
  public void importarYAsociarHechos(Long idColeccion, List<Fuente> fuentesColeccion) {
    log.info("Inicio importarYAsociarHechos para coleccion id={}", idColeccion);
    List<Hecho> hechosGuardadosTotales = new ArrayList<>();
    for (Fuente f : fuentesColeccion) {
      try {
        log.info("Importando hechos desde la fuente '{}'...", f.getNombre());
        List<Hecho> hechosDesdeFuente = f.importarHechos().collectList().block();
        if (hechosDesdeFuente == null || hechosDesdeFuente.isEmpty()) {
          continue;
        }
        for (Hecho h : hechosDesdeFuente) {
          try {
            Hecho guardado = this.hechoService.guardarHechoDinamica(h);
            if (guardado != null) hechosGuardadosTotales.add(guardado);
          } catch (Exception e) {
            log.warn("Error guardando hecho de la fuente '{}' (titulo='{}'): {}", f.getNombre(), h == null ? "<null>" : h.getTitulo(), e.getMessage());
          }
        }
      } catch (Exception e) {
        log.warn("Error importando desde la fuente '{}': {}", f.getNombre(), e.getMessage());
      }
    }

    if (!hechosGuardadosTotales.isEmpty()) {
      applicationContext.getBean(ColeccionService.class).asociarHechos(idColeccion, hechosGuardadosTotales);
    }
    log.info("Fin importarYAsociarHechos para coleccion id={}", idColeccion);
  }

  @Transactional
  public void asociarHechos(Long idColeccion, List<Hecho> hechos) {
    Optional<Coleccion> opt = this.coleccionRepository.findById(idColeccion);
    if (opt.isEmpty()) {
      throw new NotFoundException("Coleccion no encontrada id=" + idColeccion);
    }

    Coleccion coleccion = opt.get();
    coleccion.getFuentes().size();

    int inicial = coleccion.getHechos() == null ? 0 : coleccion.getHechos().size();
    int agregados = 0;
    if (hechos != null) {
      for (Hecho h : hechos) {
        Long idHecho = h.getId();
        if (idHecho == null) {
          try {
            Hecho saved = this.hechoRepository.save(h);
            coleccion.getHechos().add(saved);
            agregados++;
            this.coleccionRepository.insertHechoEnColeccion(idColeccion, saved.getId());
          } catch (Exception e) {
            log.warn("No se pudo guardar hecho sin id antes de asociar a coleccion: {}", e.getMessage());
          }
          continue;
        }

        boolean existe = coleccion.getHechos().stream().anyMatch(existing -> existing.getId() != null && existing.getId().equals(idHecho));
        if (!existe) {
          var optHecho = this.hechoRepository.findById(idHecho);
          if (optHecho.isPresent()) {
            coleccion.getHechos().add(optHecho.get());
            agregados++;
            try {
              this.coleccionRepository.insertHechoEnColeccion(idColeccion, idHecho);
            } catch (Exception e) {
              log.warn("No se pudo insertar relacion nativa coleccion-hecho para coleccion={}, hecho={}: {}", idColeccion, idHecho, e.getMessage());
            }
          } else {
            log.warn("Hecho id={} no encontrado en repo al intentar asociar a coleccion id={}", idHecho, idColeccion);
          }
        }
      }
    }

    this.coleccionRepository.saveAndFlush(coleccion);
    log.info("Coleccion id={} actualizada transaccionalmente: hechos antes={}, agregados={}, total={}", idColeccion, inicial, agregados, coleccion.getHechos().size());
  }

  @Override
  public ColeccionOutputDTO agregarFiltrosCriterio(Long id, CriterioInputDTO criterioInputDTO) {
    List<Filtro> nuevosFiltros = CriterioInputDTO.crearFiltros(criterioInputDTO);

    List<EntidadFiltro> nuevosFiltrosEntity = this.filtroMapper.toEntities(nuevosFiltros);

    Coleccion coleccion = this.findColecccionAux(id);

    coleccion.getCriterio().getFiltros().addAll(nuevosFiltrosEntity);
    coleccion.recalcularHechos();

    this.coleccionRepository.save(coleccion);
    return ColeccionOutputDTO.coleccionToDTO(coleccion);
  }

  @Override
  public ColeccionOutputDTO quitarFiltrosCriterio(Long id, CriterioInputDTO criterio) {
    Coleccion coleccion = this.findColecccionAux(id);

    List<Filtro> filtrosNuevos = CriterioInputDTO.crearFiltros(criterio);

    List<EntidadFiltro> entidadesFiltrosNuevos = this.filtroMapper.toEntities(filtrosNuevos);

    coleccion.cambiarCriterio(new Criterio(entidadesFiltrosNuevos));

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
          if(c.getHechos()==null || c.getHechos().stream().noneMatch(h -> h.getId().equals(hecho.getId()))) {
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
    List<Coleccion> colecciones = this.coleccionRepository.findColeccionsByHechos(hechos);

    AtomicBoolean resultado = new AtomicBoolean(false);

    colecciones.forEach(coleccion -> {
      List<Hecho> aux = coleccion.getHechos();
      boolean borrado = aux.remove(hecho);
      resultado.set(resultado.get() || borrado);
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

  @Override
  public List<HechoOutputDTO> buscarHechosPorConsensos(Long idColeccion, List<Consenso> consensos) {
        Coleccion coleccion = this.findColecccionAux(idColeccion);
        List<Hecho> hechosColeccion = coleccion.getHechos();
        List<Hecho> hechosFiltrados = hechosColeccion.stream()
            .filter(h -> h.getConsensos() != null && h.getConsensos().stream().anyMatch(consensos::contains))
            .toList();
        return HechoOutputDTO.mapHechoToDTO(hechosFiltrados);
    }
}
