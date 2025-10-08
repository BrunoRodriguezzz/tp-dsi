package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.ValidationException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consensuador;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
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

import ar.edu.utn.frba.dds.agregador.services.INormalizadorService;
import ar.edu.utn.frba.dds.agregador.services.IUbicacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
  public Page<HechoOutputDTO> buscarHechos(QueryParamsFiltro params, Pageable pageable) {
    //this.actualizarHechosProxy();

    Specification<Hecho> spec = HechoSpecification.conFiltros(params)
            .and(HechoSpecification.noEliminado());
    Page<Hecho> hechosPaginados = hechoRepository.findAll(spec, pageable);

    return hechosPaginados.map(HechoOutputDTO::HechoToDTO);
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

    return this.guardarHecho(hecho);
  }

  @Override
  public Hecho buscarHecho(Long id) {
      return this.buscarPorID(id);
  }

  @Override
  public Hecho guardarHecho(Hecho hecho) {
      Hecho hechoNormalizado = this.normalizadorService.normalizarHecho(hecho);
      Hecho hechoConUbicacion = this.ubicacionService.cargarUbicacion(hechoNormalizado);
      return this.hechoRepository.save(hechoConUbicacion);
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
            .flatMap(Fuente::importarHechosNuevos);

    Flux<Hecho> guardados = this.guardarHechos(hechos);

    return guardados.collectList().block();
  }

  @Override
  public List<Hecho> actualizarHechos(List<Fuente> fuentes) {
    Flux<Hecho> hechos = Flux.fromIterable(fuentes)
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
}