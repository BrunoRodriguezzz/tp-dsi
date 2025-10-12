package ar.edu.utn.frba.dds.servicioAutenticacion.services;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Categoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaSolicitudes;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.utils.EstadisticaCombinacion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Provincia;

import java.time.LocalTime;
import java.util.List;

public interface IEstadisticaService {
    EstadisticaProvinciaXColeccion provinciaConMasHechosDeUnaColeccion(Long idColeccion);
    EstadisticaCategoria categoriaConMasHechos();
    EstadisticaProvinciaXCategoria provinciaConMasHechosSegunCategoria(Long idCategoria);
    EstadisticaHoraXCategoria horaConMasHechosSegunCategoria(Long idCategoria);
    EstadisticaSolicitudes cantSolicitudesSpam();
    List<EstadisticaCombinacion> calcularEstadisticas();
    EstadisticaCombinacion crearEstadistica(EstadisticaCombinacion estadistica);
    void persistirEnCSV();
}
