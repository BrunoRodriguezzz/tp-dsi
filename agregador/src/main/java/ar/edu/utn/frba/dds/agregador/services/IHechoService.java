package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface IHechoService {
  // Busqueda de hechos
  public Page<HechoOutputDTO> buscarHechos(QueryParamsFiltro params, Pageable pageable);
  public Hecho buscarHecho(Long id);

  // Guardado de hechos
  public Hecho incorporarHecho(HechoInputDTO hecho);
  public Hecho guardarHecho(Hecho hecho);
  public Flux<Hecho> guardarHechos(Flux<Hecho> hechos);

  // Operaciones sobre hechos
  public Hecho eliminarHecho(Long id);
  public HechoOutputDTO actualizarHecho(HechoInputDTO hecho, Long id);

  // Se usa solo en los services, no en los controllers
  public List<Hecho> actualizarHechosProxy();
  public List<Hecho> actualizarHechos(List<Fuente> fuentes);
  public void consensuarHechos();
}
