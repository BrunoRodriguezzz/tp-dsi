package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.exportacion_csv;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.trazabilidad.EstadisticaSolicitudes;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Componente Spring para la exportación de estadísticas a archivos CSV
 */
@Component
public class EstadisticasCsvExporterImpl implements IEstadisticasCsvExporter {

  private static final String CSV_SEPARATOR = ",";
  private static final String CSV_LINE_SEPARATOR = "";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public void exportarTodasLasEstadisticas(
      List<EstadisticaCategoria> estadisticasCategorias,
      List<EstadisticaHoraXCategoria> estadisticasHoraXCategoria,
      List<EstadisticaProvinciaXCategoria> estadisticasProvinciaXCategoria,
      List<EstadisticaProvinciaXColeccion> estadisticasProvinciaXColeccion,
      List<EstadisticaSolicitudes> estadisticasSolicitudes,
      String directorioDestino
  ) throws IOException {

    String timestamp = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

    exportarEstadisticasCategorias(estadisticasCategorias,
        Paths.get(directorioDestino, "estadisticas_categorias_" + timestamp + ".csv").toString());

    exportarEstadisticasHoraXCategoria(estadisticasHoraXCategoria,
        Paths.get(directorioDestino, "estadisticas_hora_categoria_" + timestamp + ".csv").toString());

    exportarEstadisticasProvinciaXCategoria(estadisticasProvinciaXCategoria,
        Paths.get(directorioDestino, "estadisticas_provincia_categoria_" + timestamp + ".csv").toString());

    exportarEstadisticasProvinciaXColeccion(estadisticasProvinciaXColeccion,
        Paths.get(directorioDestino, "estadisticas_provincia_coleccion_" + timestamp + ".csv").toString());

    exportarEstadisticasSolicitudes(estadisticasSolicitudes,
        Paths.get(directorioDestino, "estadisticas_solicitudes_" + timestamp + ".csv").toString());
  }

  @Override
  public void exportarEstadisticasCategorias(List<EstadisticaCategoria> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      // Escribir encabezados
      writer.append("ID").append(CSV_SEPARATOR)
          .append("FECHA").append(CSV_SEPARATOR)
          .append("CATEGORIA_DETALLE").append(CSV_LINE_SEPARATOR);

      // Escribir datos
      for (EstadisticaCategoria estadistica : estadisticas) {
        writer.append(String.valueOf(estadistica.getId())).append(CSV_SEPARATOR)
            .append(estadistica.getFecha().format(DATE_FORMATTER)).append(CSV_SEPARATOR)
            .append(escapeCsvValue(estadistica.getCategoria().getDetalle())).append(CSV_LINE_SEPARATOR);
      }
    }
  }

  @Override
  public void exportarEstadisticasHoraXCategoria(List<EstadisticaHoraXCategoria> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      // Escribir encabezados
      writer.append("ID").append(CSV_SEPARATOR)
          .append("FECHA").append(CSV_SEPARATOR)
          .append("HORA").append(CSV_SEPARATOR)
          .append("CATEGORIA_DETALLE").append(CSV_LINE_SEPARATOR);

      // Escribir datos
      for (EstadisticaHoraXCategoria estadistica : estadisticas) {
        writer.append(String.valueOf(estadistica.getId())).append(CSV_SEPARATOR)
            .append(estadistica.getFecha().format(DATE_FORMATTER)).append(CSV_SEPARATOR)
            .append(String.valueOf(estadistica.getHora())).append(CSV_SEPARATOR)
            .append(escapeCsvValue(estadistica.getCategoria().getDetalle())).append(CSV_LINE_SEPARATOR);
      }
    }
  }

  @Override
  public void exportarEstadisticasProvinciaXCategoria(List<EstadisticaProvinciaXCategoria> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      // Escribir encabezados
      writer.append("ID").append(CSV_SEPARATOR)
          .append("FECHA").append(CSV_SEPARATOR)
          .append("PROVINCIA").append(CSV_SEPARATOR)
          .append("CATEGORIA_DETALLE").append(CSV_LINE_SEPARATOR);

      // Escribir datos
      for (EstadisticaProvinciaXCategoria estadistica : estadisticas) {
        writer.append(String.valueOf(estadistica.getId())).append(CSV_SEPARATOR)
            .append(estadistica.getFecha().format(DATE_FORMATTER)).append(CSV_SEPARATOR)
            .append(estadistica.getProvincia().toString()).append(CSV_SEPARATOR)
            .append(escapeCsvValue(estadistica.getCategoria().getDetalle())).append(CSV_LINE_SEPARATOR);
      }
    }
  }

  @Override
  public void exportarEstadisticasProvinciaXColeccion(List<EstadisticaProvinciaXColeccion> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      // Escribir encabezados
      writer.append("ID").append(CSV_SEPARATOR)
          .append("FECHA").append(CSV_SEPARATOR)
          .append("PROVINCIA").append(CSV_SEPARATOR)
          .append("COLECCION_ID").append(CSV_LINE_SEPARATOR);

      // Escribir datos
      for (EstadisticaProvinciaXColeccion estadistica : estadisticas) {
        writer.append(String.valueOf(estadistica.getId())).append(CSV_SEPARATOR)
            .append(estadistica.getFecha().format(DATE_FORMATTER)).append(CSV_SEPARATOR)
            .append(estadistica.getProvincia().toString()).append(CSV_SEPARATOR)
            .append(String.valueOf(estadistica.getColeccion().getId())).append(CSV_LINE_SEPARATOR);
      }
    }
  }

  @Override
  public void exportarEstadisticasSolicitudes(List<EstadisticaSolicitudes> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      // Escribir encabezados
      writer.append("ID").append(CSV_SEPARATOR)
          .append("FECHA").append(CSV_SEPARATOR)
          .append("SOLICITUDES_SPAM").append(CSV_LINE_SEPARATOR);

      // Escribir datos
      for (EstadisticaSolicitudes estadistica : estadisticas) {
        writer.append(String.valueOf(estadistica.getId())).append(CSV_SEPARATOR)
            .append(estadistica.getFecha().format(DATE_FORMATTER)).append(CSV_SEPARATOR)
            .append(String.valueOf(estadistica.getSolicitudes_spam())).append(CSV_LINE_SEPARATOR);
      }
    }
  }

  /**
   * Escapa valores que contienen caracteres especiales en CSV
   */
  private String escapeCsvValue(String value) {
    if (value == null) {
      return "";
    }

    // Si el valor contiene comas, comillas o saltos de línea, lo envolvemos en comillas
    if (value.contains(",") || value.contains("\"") || value.contains("") || value.contains("")) {
      // Escapar comillas dobles existentes duplicándolas
      value = value.replace("\"", "\"\"");
      return "\"" + value + "\"";
    }

    return value;
  }
}
