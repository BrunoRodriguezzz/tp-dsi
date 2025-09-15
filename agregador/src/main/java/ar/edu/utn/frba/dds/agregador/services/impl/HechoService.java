package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consensuador;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import jakarta.persistence.Id;

import java.util.*;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HechoService implements IHechoService {
  @Autowired
  private IHechoRepository hechoRepository;

  //private IFuenteService fuenteService;
  private ICategoriaRepository categoriaRepository;
  private IFuenteRepository fuenteRepository;

  public HechoService(IFuenteRepository fuenteRepository, ICategoriaRepository categoriaRepository) {
    this.fuenteRepository = fuenteRepository;
    this.categoriaRepository = categoriaRepository;
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

    Hecho hechoGuardado = this.guardarHechos(Collections.singletonList(hecho))
            .stream()
            .findFirst()
            .get();
    return hechoGuardado;
  }

  @Override
  public Hecho buscarHecho(Long id) {
    Hecho hecho = this.buscarPorID(id);
    return hecho;
  }

  @Override
  public Hecho guardarHecho(Hecho hecho) {
    Hecho hechoGuardado = this.hechoRepository.save(hecho);
    return hechoGuardado;
  }

  @Override
  public List<Hecho> guardarHechos(List<Hecho> hechos) {
    Map<String, Categoria> cacheCategorias = new HashMap<>();

    hechos.forEach(h -> {
      // Resolver todas las fuentes desde el repositorio
      if (h.getFuentes() != null && !h.getFuentes().isEmpty()) {
        List<Fuente> fuentesManaged = h.getFuentes().stream()
            .map(f -> {
              if (f != null && f.getId() != null) {
                return this.fuenteRepository.findById(f.getId()).orElseThrow();
              }
              return f;
            })
            .toList();

        h.setFuentes(fuentesManaged);
      }

      if (h.getId() == null) {
        // Buscar hecho existente
        if (h.getFuentes() != null && !h.getFuentes().isEmpty()) {
          for (int i = 0; i < h.getFuentes().size(); i++) {
            Fuente fuente = h.getFuentes().get(i);
            Long idInterno = h.getIdsInternosFuentes().get(i);

            hechoRepository.findByFuente_IdAndIdInternoFuente(fuente.getId(), idInterno)
                    .ifPresent(hechoExistente -> {
                      h.setId(hechoExistente.getId());

                      if (h.getCategoria() != null && h.getCategoria().getId() == null) {
                        h.getCategoria().setId(hechoExistente.getCategoria().getId());
                      }
                    });

            if (h.getId() != null) break; // corta el loop cuando lo encuentra
          }
        }

        if (h.getCategoria() != null && h.getCategoria().getId() == null) {
          String titulo = h.getCategoria().getTitulo();

          Categoria categoria = cacheCategorias.computeIfAbsent(titulo, t ->
              categoriaRepository.findByTitulo(t)
                  .orElseGet(() -> h.getCategoria())
          );

          h.setCategoria(categoria);
        }
      }
    });

    return this.hechoRepository.saveAll(hechos);
  }


  @Override
  public void consensuarHechos() {
    List<Hecho> hechosGuardados = this.hechoRepository.findAll();
    List<Hecho> hechosConsensuados = Consensuador.getInstance().consensuarHechos(this.fuenteRepository.findAll(), hechosGuardados); // lo puedo cambiar por el repo de la fuente
    this.hechoRepository.saveAll(hechosConsensuados);
  }

  @Override
  public List<Hecho> buscarHechosGuardadosFuente(Fuente fuente){
    List<Hecho> hechos = this.hechoRepository.findByFuente(fuente);
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
