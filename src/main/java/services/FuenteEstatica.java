package services;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import models.entities.valueObjectsHecho.Origen;
import models.entities.valueObjectsHecho.Categoria;
import models.entities.valueObjectsHecho.Ubicacion;
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

    public List<Hecho> importarHechos() {
        try (CSVReader lector = this.crearLectorCSV()) {
            List<Hecho> hechos = this.instanciarHechosSegunCSV(lector);
            return hechos;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de lectura: " + e.getMessage()); //TODO: Revisar JOPTIONPANE
            return null;
        } catch (CsvException e) {
            JOptionPane.showMessageDialog(null, "Error CSV: " + e.getMessage());
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
            return null;
        }
         //TODO: Tirar excepción si falla?
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

    private CSVReader crearLectorCSV() throws IOException {
        CSVReader reader = new CSVReaderBuilder(new FileReader(rutaArchivo))
                .withSkipLines(1) // Saltar encabezados
                .withCSVParser(new com.opencsv.CSVParserBuilder()
                        .withSeparator(',')
                        .withQuoteChar('\"')
                        .build())
                .build();
        return reader;
    }

    private List<Hecho> instanciarHechosSegunCSV(CSVReader lector) throws IOException, CsvException {
        List<Hecho> hechos = new java.util.ArrayList<>();
        List<String[]> filas = lector.readAll();
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
                hechos.add(hecho);
            }
        }
        return hechos;
    }
}