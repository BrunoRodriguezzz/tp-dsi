
package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.FuenteOutputDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.List;

public interface IFuenteService {
  List<Hecho> buscarHechos();
  List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco);
  List<Hecho> buscarHechosFuente(TipoFuente tipoFuente);
  List<Hecho> buscarHechosFuente(String nombre);
  List<Hecho> buscarHechosProxy();
  Fuente findById(Long id);
  Fuente buscarFuente(String nombre);
  List<Fuente> buscarFuentes();
  Fuente incorporarFuente(FuenteInputDTO fuenteInputDTO);
  void eliminarHecho(Hecho hecho);
  List<FuenteOutputDTO> buscarFuentesOutput();


  Flux<Hecho> buscarHechosStream();
  Flux<Hecho> buscarNuevosHechosStream(LocalDateTime ultimaFechaRefresco);
  Flux<Hecho> buscarHechosFuenteStream(TipoFuente tipoFuente);
  Flux<Hecho> buscarHechosFuenteStream(String nombre);


}
