package ar.edu.utn.frba.dds.servicioAutenticacion.services;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaSolicitudes;
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
