package services;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import models.entities.enums.Origen;
import models.entities.filtros.Categoria;
import models.entities.filtros.Ubicacion;
import models.entities.hechos.Coleccion;
import models.entities.hechos.Hecho;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FuenteEstatica {
    private String rutaArchivo;

    public FuenteEstatica(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Integer cargarColeccion(Coleccion coleccion) {   // TODO Usar un importador de hechos.
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(rutaArchivo))
                .withSkipLines(1) // Saltar encabezados
                .withCSVParser(new com.opencsv.CSVParserBuilder()
                        .withSeparator(',')
                        .withQuoteChar('\"')
                        .build())
                .build()) {

            List<String[]> filas = reader.readAll();

            for (String[] campos : filas) {
                if (campos.length >= 6) {
                    Hecho hecho = new Hecho(
                            campos[0],
                            campos[1],
                            new Categoria(campos[2]),
                            new Ubicacion(campos[3], campos[4]),
                            convertirFecha(campos[5]),
                            Origen.DATASET
                    );
                    coleccion.agregarHecho(hecho);
                }
            }

            return 0;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de lectura: " + e.getMessage());
        } catch (CsvException e) {
            JOptionPane.showMessageDialog(null, "Error CSV: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
        return -1; // Retorno de error
    }

    private static LocalDate convertirFecha(String fechaStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(fechaStr, formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en formato de fecha: " + fechaStr);
            return LocalDate.now(); // O manejar el error como prefieras
        }
    }
}