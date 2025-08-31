package ar.edu.utn.frba.dds.servicioEstadisticas.services;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Provincia;

import java.time.LocalTime;

public interface IEstadisticaService {
    Provincia provinciaConMasHechosDeUnaColeccion(Long idColeccion);
    Categoria categoriaConMasHechos();
    Provincia provinciaConMasHechosSegunCategoria(Long idCategoria);
    LocalTime horaConMasHechosSegunCategoria(Long idCategoria);
    Integer cantSolicitudesSpam();
}
