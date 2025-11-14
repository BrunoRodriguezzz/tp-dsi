package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.ValidationException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consensuador;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.specifications.HechoSpecification;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

import java.util.*;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.agregador.services.INormalizadorService;
import ar.edu.utn.frba.dds.agregador.services.IUbicacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class HechoService implements IHechoService {
  @Autowired
  private IUbicacionService ubicacionService;
  @Autowired
  private INormalizadorService normalizadorService;

  private final IHechoRepository hechoRepository;
  private final IFuenteRepository fuenteRepository;
  private final ICategoriaRepository categoriaRepository;

  public HechoService(IFuenteRepository fuenteRepository, IHechoRepository hechoRepository, ICategoriaRepository categoriaRepository) {
    this.fuenteRepository = fuenteRepository;
    this.hechoRepository = hechoRepository;
    this.categoriaRepository = categoriaRepository;
  }

  @Override
  public Page<HechoOutputDTO> buscarHechos(QueryParamsFiltro params, Pageable pageable) {
    this.actualizarHechosProxy();

    Specification<Hecho> spec = HechoSpecification.conFiltros(params)
            .and(HechoSpecification.noEliminado());
    Page<Hecho> hechosPaginados = hechoRepository.findAll(spec, pageable);

    return hechosPaginados.map(HechoOutputDTO::HechoToDTO);
  }

  @Override
  public List<HechoOutputDTO> buscarHechosIndependientes() {
    List<Hecho> hechos = this.hechoRepository.buscarHechosIndependientes(HechoSpecification.noEliminado());
    return hechos.stream().map(HechoOutputDTO::HechoToDTO).collect(Collectors.toList());
  }

  @Override
  public Hecho incorporarHecho(HechoInputDTO hechoDTO) {
    Contribuyente contribuyente = crearContribuyente(hechoDTO);
    List<Fuente> fuentesDinamicas = this.fuenteRepository.findByTipoFuente(TipoFuente.DINAMICA);
    if (fuentesDinamicas.isEmpty()) {
      throw new NotFoundException("No se encontró una fuente dinámica para asociar el hecho.");
    }
    Fuente fuente = fuentesDinamicas.get(0);
    Hecho hecho = HechoInputDTO.DTOToHecho(hechoDTO, contribuyente, fuente);
    return this.guardarHechoDinamica(hecho);
  }

  @Override
  public Hecho buscarHecho(Long id) {
      return this.buscarPorID(id);
  }

  @Override
  public Hecho guardarHechoDinamica(Hecho hecho) {
    Hecho hechoNormalizado = this.normalizadorService.normalizarHecho(hecho);
    return this.hechoRepository.save(hechoNormalizado);
  }

  @Override
  public Hecho guardarHecho(Hecho hecho) {
      Hecho hechoNormalizado = this.normalizadorService.normalizarHecho(hecho);
      if(!hechoNormalizado.getEstaEliminado() && hecho.getEstaEliminado()) {
        hechoNormalizado.eliminar();
      }
      Hecho hechoConUbicacion = this.ubicacionService.cargarUbicacion(hechoNormalizado);
      return this.hechoRepository.save(hechoConUbicacion);
  }

  @Transactional
  @Override
  public Flux<Hecho> guardarHechos(Flux<Hecho> hechosFlux) {
    // Entrada fría -> la convertimos en compartida (un solo subscribe aguas arriba)
    Flux<Hecho> entradaCompartida = hechosFlux
            .doOnSubscribe(s -> log.info("🔁 Inicio de pipeline guardarHechos"))
            .doOnError(e -> log.error("❌ Error en flujo de entrada de hechos: {}", e.getMessage(), e))
            .publish()
            .refCount(1); // primer subscriber activa la cadena, posteriores comparten

    Flux<Hecho> normalizados = this.normalizadorService
            .normalizarHechosReactivo(entradaCompartida)
            .doOnSubscribe(s -> log.info("🔁 Normalización: suscrito"))
            .doOnError(e -> log.error("❌ Error durante la normalización reactiva: {}", e.getMessage(), e))
            .doOnComplete(() -> log.info("🔁 Normalización completada"));

    Flux<Hecho> conUbicaciones = this.ubicacionService
            .obtenerUbicacionesReactivo(normalizados)
            .doOnSubscribe(s -> log.info("🔁 Obtención de ubicaciones: suscrito"))
            .doOnError(e -> log.error("❌ Error durante la obtención de ubicaciones: {}", e.getMessage(), e))
            .doOnComplete(() -> log.info("🔁 Obtención de ubicaciones completada"));

    return conUbicaciones
            .collectList() // materializa una sola vez el flujo compartido aguas arriba
            .doOnNext(list -> log.info("🔁 Resultado del pipeline - cantidad de hechos antes de guardar: {}", list == null ? 0 : list.size()))
            .flatMapMany(hechosParaGuardar -> {
              if (hechosParaGuardar == null || hechosParaGuardar.isEmpty()) {
                log.warn("⚠️ No hay hechos para guardar (pipeline vacío o con errores). hechosParaGuardar={}", 0);
                return Flux.empty();
              }

              log.info("🗂 Guardando {} hechos en la base de datos...", hechosParaGuardar.size());
              hechosParaGuardar.forEach(h -> {
                if (h.getCategoria() != null) {
                  Categoria cat = h.getCategoria();
                  if (cat.getId() != null) {
                    categoriaRepository.findById(cat.getId()).ifPresent(h::setCategoria);
                  } else if (cat.getTitulo() != null) {
                    h.setCategoria(categoriaRepository.findByTituloOrCreate(cat.getTitulo()));
                  }
                }
              });
              List<Hecho> guardados = this.hechoRepository.saveAll(hechosParaGuardar);
              log.info("✅ Se guardaron {} hechos correctamente.", guardados.size());
              return Flux.fromIterable(guardados).publish().refCount(1); // compartir también la salida
            });
  }

  @Override
  public Hecho eliminarHecho(Long id) {
    if (id <= 0) throw new ValidationException("El id del hecho debe ser mayor que 0");

    return hechoRepository.findById(id)
            .map(hecho -> {
              hecho.eliminar();
              return hechoRepository.save(hecho);
            })
            .orElseThrow(() -> new NotFoundException("No se ha encontrado el hecho"));
  }

  @Override
  public HechoOutputDTO actualizarHecho(HechoInputDTO hechoDTO, Long id) {
    if (id <= 0) throw new ValidationException("El id del hecho debe ser mayor que 0");

    Hecho hechoExistente = this.buscarPorID(id);
    
    if (hechoDTO.getCategoria() != null) {
        hechoExistente.setCategoria(categoriaRepository.findByTituloOrCreate(hechoDTO.getCategoria()));
    }

    hechoExistente.actualizarDesdeDTO(hechoDTO);
    Hecho hechoGuardado = this.guardarHecho(hechoExistente);
    return HechoOutputDTO.HechoToDTO(hechoGuardado);
  }

  @Override
  public void actualizarHechoDinamica(HechoInputDTO hecho, Long id) {
    if (id <= 0) throw new ValidationException("El id del hecho debe ser mayor que 0");

    Long idDinamica = this.fuenteRepository
            .findByTipoFuente(TipoFuente.DINAMICA)
            .stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("No se encontró una fuente dinámica para asociar el hecho."))
            .getId();
    Hecho hechoExistente = this.hechoRepository
            .findByFuenteIdAndIdInternoFuente(idDinamica, id)
            .orElseThrow(() -> new NotFoundException("No se encontró el hecho en la fuente dinámica."));
    
    if (hecho.getCategoria() != null) {
        hechoExistente.setCategoria(categoriaRepository.findByTituloOrCreate(hecho.getCategoria()));
    }

    hechoExistente.actualizarDesdeDTO(hecho);
    this.guardarHechoDinamica(hechoExistente);
  }

  @Override
  public void consensuarHechos() {
    List<Hecho> hechosGuardados = this.hechoRepository.findAll();
    List<Hecho> hechosConsensuados = Consensuador.getInstance().consensuarHechos(this.fuenteRepository.findAll(), hechosGuardados);
    this.hechoRepository.saveAll(hechosConsensuados);
  }

  @Override
  public List<Hecho> actualizarHechosProxy() {
    Flux<Hecho> hechos = Flux.fromIterable(
                    this.fuenteRepository.findByTipoFuente(TipoFuente.PROXY)
            )
            .distinct(Fuente::getId) // evita llamar dos veces a la misma fuente si aparece duplicada
            .concatMap(Fuente::importarHechosNuevos); // secuencial para no disparar concurrencia innecesaria

    Flux<Hecho> guardados = this.guardarHechos(hechos);

    return guardados.collectList().block();
  }

  @Override
  public List<Hecho> actualizarHechos(List<Fuente> fuentes) {
    Flux<Hecho> hechos = Flux.fromIterable(fuentes)
            .distinct(Fuente::getId)
            .concatMap(Fuente::importarHechosNuevos);

    Flux<Hecho> guardados = this.guardarHechos(hechos);

    return guardados.collectList().block();
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

  private Contribuyente crearContribuyente(HechoInputDTO hechoDTO) {
    Contribuyente contribuyente = null;
    if(hechoDTO.getContribuyente() != null) {
        contribuyente = new Contribuyente(
                hechoDTO.getContribuyente().getNombre(),
                hechoDTO.getContribuyente().getApellido(),
                hechoDTO.getContribuyente().getFechaNacimiento()
        );
    }
    return contribuyente;
  }
}
