package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.exportacion_csv;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaSolicitudes;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface IEstadisticasCsvExporter {

  /**
   * Exporta todas las estadísticas a archivos CSV separados
   */
  void exportarTodasLasEstadisticas(
      List<EstadisticaCategoria> estadisticasCategorias,
      List<EstadisticaHoraXCategoria> estadisticasHoraXCategoria,
      List<EstadisticaProvinciaXCategoria> estadisticasProvinciaXCategoria,
      List<EstadisticaProvinciaXColeccion> estadisticasProvinciaXColeccion,
      List<EstadisticaSolicitudes> estadisticasSolicitudes,
      String directorioDestino
  ) throws IOException;

  /**
   * Exporta estadísticas de categorías
   */
  void exportarEstadisticasCategorias(List<EstadisticaCategoria> estadisticas, String nombreArchivo) throws IOException;

  /**
   * Exporta estadísticas de hora por categoría
   */
  void exportarEstadisticasHoraXCategoria(List<EstadisticaHoraXCategoria> estadisticas, String nombreArchivo) throws IOException;

  /**
   * Exporta estadísticas de provincia por categoría
   */
  void exportarEstadisticasProvinciaXCategoria(List<EstadisticaProvinciaXCategoria> estadisticas, String nombreArchivo) throws IOException;

  /**
   * Exporta estadísticas de provincia por colección
   */
  void exportarEstadisticasProvinciaXColeccion(List<EstadisticaProvinciaXColeccion> estadisticas, String nombreArchivo) throws IOException;

  /**
   * Exporta estadísticas de solicitudes
   */
  void exportarEstadisticasSolicitudes(List<EstadisticaSolicitudes> estadisticas, String nombreArchivo) throws IOException;
}
