package ar.edu.utn.frba.dds.servicioEstadisticas.services;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.EstadisticaHechos;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Provincia;

import java.time.LocalTime;
import java.util.List;

public interface IEstadisticaService {
    Provincia provinciaConMasHechosDeUnaColeccion(Long idColeccion);
    Categoria categoriaConMasHechos();
    Provincia provinciaConMasHechosSegunCategoria(Long idCategoria);
    LocalTime horaConMasHechosSegunCategoria(Long idCategoria);
    Long cantSolicitudesSpam();
    List<EstadisticaHechos> calcularEstadisticas();
    EstadisticaHechos crearEstadistica(EstadisticaHechos estadistica);
}
