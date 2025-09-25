package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IUbicacionService {
    Hecho cargarUbicacion(Hecho hecho);
    Flux<Hecho> obtenerUbicacionesReactivo(Flux<Hecho> hechos);
    List<Hecho> obtenerUbicaciones(List<Hecho> hechos);
}
