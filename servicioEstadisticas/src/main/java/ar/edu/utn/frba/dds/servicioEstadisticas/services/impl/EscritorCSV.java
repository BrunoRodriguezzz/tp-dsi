package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.EstadisticaHechos;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EscritorCSV {

  private static final String CSV_SEPARATOR = ",";
  private static final String CSV_HEADER = "coleccion,provincia,categoria,hora,cantidad_hechos";

  public static String persistirEstadisticasEnCSV(List<EstadisticaHechos> estadisticas, String nombreArchivo) throws IOException {

    // Generar nombre de archivo si no se proporciona
    if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
      String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
      nombreArchivo = "estadisticas_" + timestamp + ".csv";
    }

    // Asegurar que el archivo tenga extensión .csv
    if (!nombreArchivo.toLowerCase().endsWith(".csv")) {
      nombreArchivo += ".csv";
    }

    try (FileWriter writer = new FileWriter(nombreArchivo)) {

      // Escribir header
      writer.append(CSV_HEADER);
      writer.append(System.lineSeparator());

      // Escribir datos
      for (EstadisticaHechos estadistica : estadisticas) {
        writer.append(escaparValorCSV(estadistica.getColeccion().getDetalle()))
            .append(CSV_SEPARATOR)
            .append(escaparValorCSV(estadistica.getProvincia().toString()))
            .append(CSV_SEPARATOR)
            .append(escaparValorCSV(estadistica.getCategoria().getDetalle()))
            .append(CSV_SEPARATOR)
            .append(escaparValorCSV(String.valueOf(estadistica.getHora().toString())))
            .append(CSV_SEPARATOR)
            .append(String.valueOf(estadistica.getCantidad_hechos()))
            .append(System.lineSeparator());
      }

      writer.flush();
    }

    return nombreArchivo;
  }

  // Version sin nombre de archivo
  public String persistirEstadisticasEnCSV(List<EstadisticaHechos> estadisticas) throws IOException {
    return persistirEstadisticasEnCSV(estadisticas, null);
  }

  private static String escaparValorCSV(String valor) {
    if (valor == null) {
      return "";
    }

    // Si el valor contiene comas, comillas o saltos de línea, lo envolvemos en comillas
    if (valor.contains(",") || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
      // Escapar comillas dobles duplicándolas
      valor = valor.replace("\"", "\"\"");
      return "\"" + valor + "\"";
    }

    return valor;
  }
}
