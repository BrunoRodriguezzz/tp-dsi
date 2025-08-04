package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consensuador;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Filtro;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HechoService implements IHechoService {
  @Autowired
  private IHechoRepository hechoRepository;

  private IFuenteService fuenteService;

  public HechoService(IFuenteService fuenteService) {
    this.fuenteService = fuenteService;
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
    Hecho hecho = this.hechoRepository.buscarHecho(id);
    return hecho;
  }

  @Override
  public Hecho guardarHecho(Hecho hecho) {
    Hecho hechoGuardado = this.hechoRepository.guardarHecho(hecho);
    return hechoGuardado;
  }

  @Override
  public List<Hecho> guardarHechos(List<Hecho> hechos){
    List<Hecho> hechosGuardados = this.hechoRepository.guardarHechos(hechos);
    return hechosGuardados;
  }

  @Override
  public void consensuarHechos() {
    List<Hecho> hechosGuardados = this.hechoRepository.buscarHechos();
    List<Hecho> hechosConsensuados = Consensuador.getInstance().consensuarHechos(this.fuenteService.buscarFuentes(), hechosGuardados);
    this.hechoRepository.guardarHechos(hechosConsensuados);
  }

  @Override
  public List<Hecho> buscarHechosGuardadosFuente(Fuente fuente){
    List<Hecho> hechos = this.hechoRepository.buscarHechosGuardadosFuente(fuente);
    return hechos;
  }
}
