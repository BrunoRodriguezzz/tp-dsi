package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consensuador;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HechoService implements IHechoService {
  @Autowired
  private IHechoRepository hechoRepository;
  private IFuenteService fuenteService;
  private ICategoriaRepository categoriaRepository;

  public HechoService(IFuenteService fuenteService, ICategoriaRepository categoriaRepository) {
    this.fuenteService = fuenteService;
    this.categoriaRepository = categoriaRepository;
  }

  @Override
  public List<HechoOutputDTO> buscarHechos(QueryParamsFiltro params) {
    List<Hecho> hechos = this.fuenteService.buscarHechos();
    hechos = this.guardarHechos(hechos);

    List<Filtro> filtrosBusqueda = params.instanciarFiltros();
    List<Hecho> hechosFiltrados;

    if(!filtrosBusqueda.isEmpty()) {
      hechosFiltrados = hechos
          .stream()
          .filter(h -> filtrosBusqueda.stream().allMatch(f -> f.coincide(h)))
          .toList();
    }
    else hechosFiltrados = hechos;

    List<HechoOutputDTO> hechosDTO = HechoOutputDTO.mapHechoToDTO(hechosFiltrados);

    return hechosDTO;
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
    Fuente fuente = this.fuenteService.buscarFuente(hechoDTO.getFuente());
    Hecho hecho = HechoInputDTO.DTOToHecho(hechoDTO, contribuyente, fuente);
    Hecho hechoGuardado = this.guardarHecho(hecho);
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
  public List<Hecho> guardarHechos(List<Hecho> hechos){
    hechos.forEach(h -> {
      if (h.getId() == null) {
        // Buscar hecho existente por fuente + idInternoFuente
        hechoRepository.findByFuente_IdAndIdInternoFuente(h.getFuente().getId(), h.getIdInternoFuente())
                .ifPresent(hechoExistente -> {
                  // Reutilizar ID
                  h.setId(hechoExistente.getId());

                  // Reutilizar categoría
                  if (h.getCategoria().getId() == null) {
                    h.getCategoria().setId(hechoExistente.getCategoria().getId());
                  }
                });

        // Si no encontré el hecho, al menos resuelvo la categoría por título
        if (h.getCategoria().getId() == null) {
          categoriaRepository.findByTitulo(h.getCategoria().getTitulo())
                  .ifPresent(categoriaExistente -> h.getCategoria().setId(categoriaExistente.getId()));
        }
      }
    });

    List<Hecho> hechosGuardados = this.hechoRepository.saveAll(hechos);
    return hechosGuardados;
  }

  @Override
  public void consensuarHechos() {
    List<Hecho> hechosGuardados = this.hechoRepository.findAll();
    List<Hecho> hechosConsensuados = Consensuador.getInstance().consensuarHechos(this.fuenteService.buscarFuentes(), hechosGuardados);
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
}
