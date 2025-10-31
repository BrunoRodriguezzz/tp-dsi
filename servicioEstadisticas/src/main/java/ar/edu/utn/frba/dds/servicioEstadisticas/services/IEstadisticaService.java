package ar.edu.utn.frba.dds.servicioEstadisticas.services;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.output.*;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.utils.EstadisticaCombinacion;

import java.util.List;

public interface IEstadisticaService {
    EstadisticaProvinciaXColeccionDTO provinciaConMasHechosDeUnaColeccion(Long idColeccion);
    List<EstadisticaProvinciaXColeccionDTO> provinciasConMasHechosPorColecciones();
    EstadisticaCategoriaDTO categoriaConMasHechos();
    EstadisticaProvinciaXCategoria provinciaConMasHechosSegunCategoria(Long idCategoria);
    List<EstadisticaProvinciaXCategoriaDTO> provinciaConMasHechosSegunCategorias();
    EstadisticaHoraXCategoria horaConMasHechosSegunCategoria(Long idCategoria);
    List<EstadisticaHoraXCategoriaDTO> horaConMasHechosSegunCategorias();
    EstadisticaSolicitudesDTO cantSolicitudesSpam();
    List<EstadisticaCombinacion> calcularEstadisticas();
    EstadisticaCombinacion crearEstadistica(EstadisticaCombinacion estadistica);
    void persistirEnCSV();
}
