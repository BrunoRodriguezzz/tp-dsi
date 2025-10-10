
package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

public interface IAdapImpH {
    Flux<Hecho> importarHechos(WebClient webClient, Fuente fuente);
    Mono<Void> eliminarHecho(Hecho hecho, WebClient webClient, Fuente fuente);
    Flux<Hecho> importarHechosMismoTitulo(WebClient webClient, Fuente fuente, Hecho hechos);

    Flux<Hecho> importarNuevos(WebClient webClient, Fuente fuente);
}
