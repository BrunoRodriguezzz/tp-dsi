package models.entities.fuentes;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import models.entities.hechos.Hecho;
import models.entities.utils.Errores.ER_ValueObjects.*;
import models.entities.valueObjectsHecho.Origen;
import models.entities.valueObjectsHecho.Categoria;
import models.entities.valueObjectsHecho.Ubicacion;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class FuenteEstatica implements Fuente {
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
                try {
                    Ubicacion ubicacion = new Ubicacion(campos[3], campos[4]);
                    Categoria categoria = new Categoria(campos[2]);

                    Hecho hecho = new Hecho(
                            campos[0],
                            campos[1],
                            categoria,
                            ubicacion,
                            convertirFecha(campos[5]),
                            Origen.DATASET
                    );
                    hechos.add(hecho);
                } catch (UbicacionInvalidaException e) {
                    System.err.println("Ubicación inválida para fila: " + Arrays.toString(campos));
                }

                catch (CategoriaInvalidaException e) {
                    System.err.println("Categoria inválida para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (TituloInvalidoException e) {
                    System.err.println("Titulo inválido para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (DescripcionInvalidaException e) {
                    System.err.println("Descripcion inválido para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (FechaInvalidaException e) {
                    System.err.println("Fecha inválida para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }


            }
        }
        return hechos;
    }
}