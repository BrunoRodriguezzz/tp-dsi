package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.ValidationException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consensuador;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.HechoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.agregador.services.INormalizadorService;
import ar.edu.utn.frba.dds.agregador.services.IUbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class HechoService implements IHechoService {
  @Autowired
  private IUbicacionService ubicacionService;
  @Autowired
  private INormalizadorService normalizadorService;

  private IHechoRepository hechoRepository;
  private ICategoriaRepository categoriaRepository;
  private IFuenteRepository fuenteRepository;

  public HechoService(IFuenteRepository fuenteRepository, ICategoriaRepository categoriaRepository, IHechoRepository hechoRepository) {
    this.fuenteRepository = fuenteRepository;
    this.categoriaRepository = categoriaRepository;
    this.hechoRepository = hechoRepository;
  }

  @Override
  public List<HechoOutputDTO> buscarHechos(QueryParamsFiltro params) {
    List<Hecho> hechosProxy = this.hechoRepository.findByOrigen(Origen.PROXY); // fuenteService.buscarHechosProxy(); se podria sacar y llamar directamente al repo de hecho
    this.guardarHechos(hechosProxy);
    List<Hecho> hechos = this.hechoRepository.findAll();
    List<Filtro> filtrosBusqueda = params.instanciarFiltros();
    List<Hecho> hechosFiltrados;
    if(!filtrosBusqueda.isEmpty()) {
      hechosFiltrados = hechos
              .stream()
              .filter(h -> filtrosBusqueda.stream().allMatch(f -> f.coincide(h)))
              .toList();
    }
    else hechosFiltrados = hechos;
    return HechoOutputDTO.mapHechoToDTO(hechosFiltrados);
  }

  @Override
  public List<HechoOutputDTO> buscarHechosProxy() {
    return HechoOutputDTO.mapHechoToDTO(this.pedirHechosProxy(fuenteRepository.findAll().stream().filter(f -> f.getTipoFuente().equals(TipoFuente.PROXY)).toList())); //(this.fuenteService.buscarHechosProxy()); // si cambio lo de arriba se iria
  }

  @Override
  public List<HechoOutputDTO> buscarHechosIndependientes() { // metodo muerto?
    return null;
  }

  @Override
  public Hecho incorporarHecho(HechoInputDTO hechoDTO) {
    Contribuyente contribuyente = null;
    if(hechoDTO.getContribuyente() != null) {
      try {
        contribuyente = new Contribuyente(
            hechoDTO.getContribuyente().getNombre(),
            hechoDTO.getContribuyente().getApellido(),
            hechoDTO.getContribuyente().getFechaNacimiento()
        );
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    //List<Fuente> fuentes = this.fuenteRepository.findAll().stream().filter(f -> hechoDTO.getFuentes().contains(FuenteOutputDTO.toOutputDTO(f))).toList();
    Fuente fuente = this.fuenteRepository.findByTipoFuente(TipoFuente.DINAMICA).get(0); // supongo que solo se incorporara de la unica fuente dinamica
    Hecho hecho = HechoInputDTO.DTOToHecho(hechoDTO, contribuyente, fuente);

      return this.guardarHechos(Collections.singletonList(hecho))
              .stream()
              .findFirst()
              .orElse(null);
  }

  @Override
  public Hecho buscarHecho(Long id) {
      return this.buscarPorID(id);
  }

  @Override
  public Hecho guardarHecho(Hecho hecho) {
      return this.hechoRepository.save(hecho);
  }

  @Override
  public List<Hecho> guardarHechos(List<Hecho> hechos) {
    List<Hecho> hechosNormalizados = hechos.stream()
            .map(h -> this.normalizadorService.normalizarHecho(h))
            .toList();
    this.ubicacionService.obtenerUbicacionesReactivo(Flux.fromIterable(hechosNormalizados)).blockLast();
    Map<String, Categoria> cacheCategorias = new HashMap<>();

    hechosNormalizados.forEach(h -> {
      if (h.getId() == null) {
        // Buscar hecho existente
        if (h.getFuenteSet() != null && !h.getFuenteSet().isEmpty()) {
          Long fuenteID = h.getFuenteSet().stream().findFirst().get().getFuente().getId();
          Long idInterno = h.getFuenteSet().stream().findFirst().get().getIdInternoFuente();
          hechoRepository.findByFuenteIdAndIdInternoFuente(fuenteID, idInterno)
                  .ifPresent(hechoExistente -> {
                    h.setId(hechoExistente.getId());

                    if (h.getCategoria() != null && h.getCategoria().getId() == null) {
                      h.getCategoria().setId(hechoExistente.getCategoria().getId());
                    }
                  });
        }

        if (h.getCategoria() != null && h.getCategoria().getId() == null) {
          String titulo = h.getCategoria().getTitulo();

          Categoria categoria = cacheCategorias.computeIfAbsent(titulo, t ->
              categoriaRepository.findByTitulo(t)
                  .orElseGet(h::getCategoria)
          );

          h.setCategoria(categoria);
        }
      }
    });

    return this.hechoRepository.saveAll(hechos);
  }

  @Override
  public List<Hecho> guardarHechosReactivo(Flux<Hecho> hechos) {
    // Normalizar los hechos de forma reactiva
    Flux<Hecho> hechosNormalizados = normalizadorService.normalizarHechosReactivo(hechos);

    // Procesar ubicaciones de forma reactiva (sin bloquear)
    Flux<Hecho> hechosConUbicaciones = ubicacionService.obtenerUbicacionesReactivo(hechosNormalizados);

    // Recolectar todos los hechos en una lista (aquí termina la parte reactiva)
    List<Hecho> listaHechos = hechosConUbicaciones.collectList().block();

    if(listaHechos == null) {
      return null;
    }

    // Procesamiento imperativo (no reactivo)
    return guardarHechosImperativo(listaHechos);
  }

  private List<Hecho> guardarHechosImperativo(List<Hecho> hechos) {
    List<Hecho> resultado = new ArrayList<>();

    for (Hecho hecho : hechos) {
      try {
        // Si el hecho ya tiene ID, agregarlo directamente al resultado
        if (hecho.getId() != null) {
          resultado.add(hecho);
          continue;
        }

        // Procesar hecho sin ID
        procesarHechoSinIdImperativo(hecho);

        // Asegurarse de que la categoría esté gestionada
        gestionarCategoriaImperativo(hecho);

        // Guardar el hecho
        Hecho hechoGuardado = guardarHechoImperativo(hecho);
        resultado.add(hechoGuardado);

      } catch (Exception e) {
        // Manejar errores individuales sin detener todo el proceso
        System.err.println("Error al procesar hecho: " + e.getMessage());
        // Agregar el hecho original al resultado para no perderlo
        resultado.add(hecho);
      }
    }

    return resultado;
  }

  private void procesarHechoSinIdImperativo(Hecho hecho) {
    // Buscar hecho existente por fuente
    if (hecho.getFuenteSet() != null && !hecho.getFuenteSet().isEmpty()) {
      HechoFuente hf = hecho.getFuenteSet()
              .stream()
              .findFirst()
              .orElseThrow(() -> new ValidationException("Llego un hecho sin Fuente"));
      Long fuenteID = hf.getFuente().getId();
      Long idInterno = hf.getIdInternoFuente();

      Optional<Hecho> hechoExistenteOptional = hechoRepository.findByFuenteIdAndIdInternoFuente(fuenteID, idInterno);

      if (hechoExistenteOptional.isPresent()) {
        Hecho hechoExistente = hechoExistenteOptional.get();
        hecho.setId(hechoExistente.getId());

        // Si la categoría del hecho existente tiene ID, usarla
        if (hechoExistente.getCategoria() != null && hechoExistente.getCategoria().getId() != null) {
          hecho.setCategoria(hechoExistente.getCategoria());
        }
      }
    }
  }

  private void gestionarCategoriaImperativo(Hecho hecho) {
    if (hecho.getCategoria() != null) {
      Categoria categoria = hecho.getCategoria();

      // Si la categoría no tiene ID, buscarla o crearla
      if (categoria.getId() == null) {
        Categoria categoriaGestionada = categoriaRepository.findByTituloOrCreate(categoria.getTitulo());
        hecho.setCategoria(categoriaGestionada);
      } else {
        // Si la categoría tiene ID, asegurarse de que esté gestionada
        Categoria categoriaGestionada = categoriaRepository.findById(categoria.getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoria.getId()));
        hecho.setCategoria(categoriaGestionada);
      }
    }
  }

  private Hecho guardarHechoImperativo(Hecho hecho) {
    try {
      return hechoRepository.save(hecho);
    } catch (DataIntegrityViolationException e) {
      // Si hay un error al guardar, intentar recuperar el hecho existente
      if (hecho.getFuenteSet() != null && !hecho.getFuenteSet().isEmpty()) {
        HechoFuente hf = hecho.getFuenteSet().iterator().next();
        Long fuenteID = hf.getFuente().getId();
        Long idInterno = hf.getIdInternoFuente();

        Optional<Hecho> hechoExistente = hechoRepository.findByFuenteIdAndIdInternoFuente(fuenteID, idInterno);
        return hechoExistente.orElseThrow(() -> e);
      }
      throw e;
    }
  }

  @Override
  public void consensuarHechos() {
    List<Hecho> hechosGuardados = this.hechoRepository.findAll();
    List<Hecho> hechosConsensuados = Consensuador.getInstance().consensuarHechos(this.fuenteRepository.findAll(), hechosGuardados); // lo puedo cambiar por el repo de la fuente
    this.hechoRepository.saveAll(hechosConsensuados);
  }

  @Override
  public List<Hecho> buscarHechosGuardadosFuente(List<Fuente> fuentes){
    List<Hecho> hechos = this.hechoRepository.findByFuentes(fuentes);
    return hechos;
  }

  private Hecho buscarPorID(Long id){
    Optional<Hecho> hechoOptional = this.hechoRepository.findById(id);
    if(hechoOptional.isPresent()) {
      return hechoOptional.get();
    }
    else {
      throw new NotFoundException("Hecho no encontrado.");
    }
  }

  private List<Hecho> pedirHechosProxy(List<Fuente> fuentes) {
    return fuentes.stream()
        .map(fuente -> fuente.importarHechos().toStream().toList())
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
