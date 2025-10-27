package ar.edu.utn.frba.dds.servicioEstadisticas.services;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output.EstadisticaCategoriaDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output.EstadisticaSolicitudesDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.utils.EstadisticaCombinacion;

import java.util.List;

public interface IEstadisticaService {
    EstadisticaProvinciaXColeccion provinciaConMasHechosDeUnaColeccion(Long idColeccion);
    EstadisticaCategoriaDTO categoriaConMasHechos();
    EstadisticaProvinciaXCategoria provinciaConMasHechosSegunCategoria(Long idCategoria);
    EstadisticaHoraXCategoria horaConMasHechosSegunCategoria(Long idCategoria);
    EstadisticaSolicitudesDTO cantSolicitudesSpam();
    List<EstadisticaCombinacion> calcularEstadisticas();
    EstadisticaCombinacion crearEstadistica(EstadisticaCombinacion estadistica);
    void persistirEnCSV();
}
