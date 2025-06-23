package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public interface IAdapImpH {
    public List<Hecho> importarHechos(WebClient webClient);
    public List<Hecho> buscarNuevosHechos(LocalDateTime ultimaFechaRefresco, WebClient webClient);
    public void eliminarHecho(Hecho hecho, WebClient webClient);
}
