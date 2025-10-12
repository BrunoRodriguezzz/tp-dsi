
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
  // Conseguir fuentes
  List<FuenteOutputDTO> buscarFuentesOutput(Boolean nuevos);

  // Operaciones sobre fuentes
  Fuente incorporarFuente(FuenteInputDTO fuenteInputDTO);
  void eliminarHecho(Hecho hecho);

  // Privados
  Flux<Hecho> buscarHechosFuenteStream(String nombre);
}
