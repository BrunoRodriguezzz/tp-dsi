package ar.edu.utn.frba.dds.servicioEstadisticas.services;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaSolicitudes;
import java.util.List;

public interface IEstadisticasCsvService {
  public String exportarEstadisticas(
      List<EstadisticaCategoria> estadisticasCategorias,
      List<EstadisticaHoraXCategoria> estadisticasHoraXCategoria,
      List<EstadisticaProvinciaXCategoria> estadisticasProvinciaXCategoria,
      List<EstadisticaProvinciaXColeccion> estadisticasProvinciaXColeccion,
      List<EstadisticaSolicitudes> estadisticasSolicitudes,
      String directorioDestino);
}
