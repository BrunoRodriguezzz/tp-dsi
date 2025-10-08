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
import ar.edu.utn.frba.dds.agregador.models.repositories.specifications.HechoSpecification;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.agregador.services.INormalizadorService;
import ar.edu.utn.frba.dds.agregador.services.IUbicacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
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
    this.actualizarHechosProxy();

    Specification<Hecho> spec = HechoSpecification.conFiltros(params);
    List<Hecho> hechosFiltrados = hechoRepository.findAll(spec);

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

    Fuente fuente = this.fuenteRepository.findByTipoFuente(TipoFuente.DINAMICA).get(0); // supongo que solo se incorporara de la unica fuente dinamica
    Hecho hecho = HechoInputDTO.DTOToHecho(hechoDTO, contribuyente, fuente);

      return this.guardarHechos(Flux.fromIterable(Collections.singletonList(hecho)))
              .blockFirst();
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
  public Flux<Hecho> guardarHechos(Flux<Hecho> hechosFlux) {
    return this.ubicacionService
            .obtenerUbicacionesReactivo(
                    this.normalizadorService.normalizarHechosReactivo(hechosFlux)
                            .doOnError(e -> log.error("❌ Error al normalizar hechos", e))
                            .onErrorContinue((e, h) -> log.warn("⚠️ Error procesando hecho: {}", h, e))
            )
            .doOnError(e -> log.error("❌ Error al obtener ubicaciones", e))
            .onErrorResume(e -> {
              log.error("🚨 Error general en pipeline de guardarHechos", e);
              return Flux.empty();
            })
            .collectList()
            .flatMapMany(hechosParaGuardar -> {
              if (hechosParaGuardar.isEmpty()) {
                log.warn("⚠️ No hay hechos para guardar (pipeline vacío o con errores).");
                return Flux.empty();
              }

              log.info("🗂 Guardando {} hechos en la base de datos...", hechosParaGuardar.size());
              List<Hecho> guardados = this.hechoRepository.saveAll(hechosParaGuardar);
              log.info("✅ Se guardaron {} hechos correctamente.", guardados.size());
              return Flux.fromIterable(guardados);
            });
  }

  @Override
  public void consensuarHechos() {
    List<Hecho> hechosGuardados = this.hechoRepository.findAll();
    List<Hecho> hechosConsensuados = Consensuador.getInstance().consensuarHechos(this.fuenteRepository.findAll(), hechosGuardados); // lo puedo cambiar por el repo de la fuente
    this.hechoRepository.saveAll(hechosConsensuados);
  }

  @Override
  public List<Hecho> actualizarHechosProxy() {
    Flux<Hecho> hechos = Flux.fromIterable(
                    this.fuenteRepository.findByTipoFuente(TipoFuente.PROXY)
            )
            .flatMap(Fuente::importarHechosNuevos);

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

  private List<Hecho> pedirHechosProxy(List<Fuente> fuentes) {
    return fuentes.stream()
        .map(fuente -> fuente.importarHechos().toStream().toList())
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
