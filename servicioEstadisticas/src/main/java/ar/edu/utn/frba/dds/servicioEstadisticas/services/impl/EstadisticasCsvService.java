package ar.edu.utn.frba.dds.servicioAutenticacion.services.impl;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.exportacion_csv.IEstadisticasCsvExporter;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaSolicitudes;
import ar.edu.utn.frba.dds.servicioAutenticacion.services.IEstadisticasCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class EstadisticasCsvService implements IEstadisticasCsvService {

  private final IEstadisticasCsvExporter csvExporter;

  @Autowired
  public EstadisticasCsvService(IEstadisticasCsvExporter csvExporter) {
    this.csvExporter = csvExporter;
  }

  public String exportarEstadisticas(
      List<EstadisticaCategoria> estadisticasCategorias,
      List<EstadisticaHoraXCategoria> estadisticasHoraXCategoria,
      List<EstadisticaProvinciaXCategoria> estadisticasProvinciaXCategoria,
      List<EstadisticaProvinciaXColeccion> estadisticasProvinciaXColeccion,
      List<EstadisticaSolicitudes> estadisticasSolicitudes,
      String directorioDestino) {

    try {
      csvExporter.exportarTodasLasEstadisticas(
          estadisticasCategorias,
          estadisticasHoraXCategoria,
          estadisticasProvinciaXCategoria,
          estadisticasProvinciaXColeccion,
          estadisticasSolicitudes,
          directorioDestino
      );

      int totalRegistros =
          (estadisticasCategorias != null ? estadisticasCategorias.size() : 0) +
              (estadisticasHoraXCategoria != null ? estadisticasHoraXCategoria.size() : 0) +
              (estadisticasProvinciaXCategoria != null ? estadisticasProvinciaXCategoria.size() : 0) +
              (estadisticasProvinciaXColeccion != null ? estadisticasProvinciaXColeccion.size() : 0) +
              (estadisticasSolicitudes != null ? estadisticasSolicitudes.size() : 0);

      return String.format("Exportación completada exitosamente. %d registros exportados en 5 archivos CSV en el directorio: %s",
          totalRegistros, directorioDestino);

    } catch (IOException e) {
      throw new RuntimeException("Error durante la exportación: " + e.getMessage());
    }
  }
}
