package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

public interface IHechoService {
  public List<HechoOutputDTO> buscarHechos(QueryParamsFiltro params);
  public Hecho incorporarHecho(HechoInputDTO hecho);
  public Hecho guardarHecho(Hecho hecho);
  public Hecho buscarHecho(Long id);
  public Flux<Hecho> guardarHechos(Flux<Hecho> hechos);
  public void consensuarHechos();
  public List<HechoOutputDTO> buscarHechosIndependientes();
  public List<HechoOutputDTO> buscarHechosProxy();
  public List<Hecho> actualizarHechosProxy();
}
