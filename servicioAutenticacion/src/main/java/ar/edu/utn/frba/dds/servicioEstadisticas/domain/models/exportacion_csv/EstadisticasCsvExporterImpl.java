package ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.exportacion_csv;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Categoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.HoraDelDia;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones.Provincia;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaHoraXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXCategoria;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaProvinciaXColeccion;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.trazabilidad.EstadisticaSolicitudes;
import java.util.Map;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class EstadisticasCsvExporterImpl implements IEstadisticasCsvExporter {

  private static final String CSV_SEPARATOR = ",";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public void exportarTodasLasEstadisticas(
      List<EstadisticaCategoria> estadisticasCategorias,
      List<EstadisticaHoraXCategoria> estadisticasHoraXCategoria,
      List<EstadisticaProvinciaXCategoria> estadisticasProvinciaXCategoria,
      List<EstadisticaProvinciaXColeccion> estadisticasProvinciaXColeccion,
      List<EstadisticaSolicitudes> estadisticasSolicitudes,
      String directorioDestino) throws IOException {

    String timestamp = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

    if (estadisticasCategorias != null && !estadisticasCategorias.isEmpty()) {
      exportarEstadisticasCategorias(estadisticasCategorias,
          directorioDestino + "/estadisticas_categorias_" + timestamp + ".csv");
    }

    if (estadisticasHoraXCategoria != null && !estadisticasHoraXCategoria.isEmpty()) {
      exportarEstadisticasHoraXCategoria(estadisticasHoraXCategoria,
          directorioDestino + "/estadisticas_hora_x_categoria_" + timestamp + ".csv");
    }

    if (estadisticasProvinciaXCategoria != null && !estadisticasProvinciaXCategoria.isEmpty()) {
      exportarEstadisticasProvinciaXCategoria(estadisticasProvinciaXCategoria,
          directorioDestino + "/estadisticas_provincia_x_categoria_" + timestamp + ".csv");
    }

    if (estadisticasProvinciaXColeccion != null && !estadisticasProvinciaXColeccion.isEmpty()) {
      exportarEstadisticasProvinciaXColeccion(estadisticasProvinciaXColeccion,
          directorioDestino + "/estadisticas_provincia_x_coleccion_" + timestamp + ".csv");
    }

    if (estadisticasSolicitudes != null && !estadisticasSolicitudes.isEmpty()) {
      exportarEstadisticasSolicitudes(estadisticasSolicitudes,
          directorioDestino + "/estadisticas_solicitudes_" + timestamp + ".csv");
    }
  }

  @Override
  public void exportarEstadisticasCategorias(List<EstadisticaCategoria> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      writer.write("ID" + CSV_SEPARATOR +
          "FECHA" + CSV_SEPARATOR +
          "CATEGORIA_ID" + CSV_SEPARATOR +
          "CATEGORIA_DETALLE" + CSV_SEPARATOR +
          "CANTIDAD_HECHOS" + "\n");

      for (EstadisticaCategoria estadistica : estadisticas) {
        for (Map.Entry<Categoria, Long> entry : estadistica.getCategoriasConHechos().entrySet()) {
          Categoria categoria = entry.getKey();
          Long cantidad = entry.getValue();

          writer.write(estadistica.getId() + CSV_SEPARATOR +
              estadistica.getFecha().format(DATE_FORMATTER) + CSV_SEPARATOR +
              categoria.getId() + CSV_SEPARATOR +
              escapeCsvValue(categoria.getDetalle()) + CSV_SEPARATOR +
              cantidad + "\n");
        }
      }
    }
  }

  @Override
  public void exportarEstadisticasHoraXCategoria(List<EstadisticaHoraXCategoria> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      writer.write("ID" + CSV_SEPARATOR +
          "FECHA" + CSV_SEPARATOR +
          "CATEGORIA_ID" + CSV_SEPARATOR +
          "CATEGORIA_DETALLE" + CSV_SEPARATOR +
          "HORA_ENUM" + CSV_SEPARATOR +
          "HORA_VALOR" + CSV_SEPARATOR +
          "CANTIDAD_HECHOS" + "\n");

      for (EstadisticaHoraXCategoria estadistica : estadisticas) {
        for (Map.Entry<HoraDelDia, Long> entry : estadistica.getHorasConHechos().entrySet()) {
          HoraDelDia hora = entry.getKey();
          Long cantidad = entry.getValue();

          writer.write(estadistica.getId() + CSV_SEPARATOR +
              estadistica.getFecha().format(DATE_FORMATTER) + CSV_SEPARATOR +
              estadistica.getCategoria().getId() + CSV_SEPARATOR +
              escapeCsvValue(estadistica.getCategoria().getDetalle()) + CSV_SEPARATOR +
              hora.name() + CSV_SEPARATOR +
              hora.getValue() + CSV_SEPARATOR +
              cantidad + "\n");
        }
      }
    }
  }

  @Override
  public void exportarEstadisticasProvinciaXCategoria(List<EstadisticaProvinciaXCategoria> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      writer.write("ID" + CSV_SEPARATOR +
          "FECHA" + CSV_SEPARATOR +
          "CATEGORIA_ID" + CSV_SEPARATOR +
          "CATEGORIA_DETALLE" + CSV_SEPARATOR +
          "PROVINCIA_ENUM" + CSV_SEPARATOR +
          "PROVINCIA_VALOR" + CSV_SEPARATOR +
          "CANTIDAD_HECHOS" + "\n");

      for (EstadisticaProvinciaXCategoria estadistica : estadisticas) {
        for (Map.Entry<Provincia, Long> entry : estadistica.getProvinciasConHechos().entrySet()) {
          Provincia provincia = entry.getKey();
          Long cantidad = entry.getValue();

          writer.write(estadistica.getId() + CSV_SEPARATOR +
              estadistica.getFecha().format(DATE_FORMATTER) + CSV_SEPARATOR +
              estadistica.getCategoria().getId() + CSV_SEPARATOR +
              escapeCsvValue(estadistica.getCategoria().getDetalle()) + CSV_SEPARATOR +
              provincia.name() + CSV_SEPARATOR +
              escapeCsvValue(provincia.getValue()) + CSV_SEPARATOR +
              cantidad + "\n");
        }
      }
    }
  }

  @Override
  public void exportarEstadisticasProvinciaXColeccion(List<EstadisticaProvinciaXColeccion> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      writer.write("ID" + CSV_SEPARATOR +
          "FECHA" + CSV_SEPARATOR +
          "COLECCION_ID" + CSV_SEPARATOR +
          "COLECCION_DETALLE" + CSV_SEPARATOR +
          "PROVINCIA_ENUM" + CSV_SEPARATOR +
          "PROVINCIA_VALOR" + CSV_SEPARATOR +
          "CANTIDAD_HECHOS" + "\n");

      for (EstadisticaProvinciaXColeccion estadistica : estadisticas) {
        for (Map.Entry<Provincia, Long> entry : estadistica.getProvinciasConHechos().entrySet()) {
          Provincia provincia = entry.getKey();
          Long cantidad = entry.getValue();

          writer.write(estadistica.getId() + CSV_SEPARATOR +
              estadistica.getFecha().format(DATE_FORMATTER) + CSV_SEPARATOR +
              estadistica.getColeccion().getId() + CSV_SEPARATOR +
              escapeCsvValue(estadistica.getColeccion().getDetalle()) + CSV_SEPARATOR +
              provincia.name() + CSV_SEPARATOR +
              escapeCsvValue(provincia.getValue()) + CSV_SEPARATOR +
              cantidad + "\n");
        }
      }
    }
  }

  @Override
  public void exportarEstadisticasSolicitudes(List<EstadisticaSolicitudes> estadisticas, String nombreArchivo) throws IOException {
    try (FileWriter writer = new FileWriter(nombreArchivo)) {
      writer.write("ID" + CSV_SEPARATOR +
          "FECHA" + CSV_SEPARATOR +
          "SOLICITUDES_SPAM" + CSV_SEPARATOR +
          "SOLICITUDES_NO_SPAM" + "\n");

      for (EstadisticaSolicitudes estadistica : estadisticas) {
        writer.write(estadistica.getId() + CSV_SEPARATOR +
            estadistica.getFecha().format(DATE_FORMATTER) + CSV_SEPARATOR +
            estadistica.getSolicitudes_spam() + CSV_SEPARATOR +
            estadistica.getSolicitudes_no_spam() + "\n");
      }
    }
  }

  private String escapeCsvValue(String value) {
    if (value == null) {
      return "";
    }

    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    return value;
  }
}