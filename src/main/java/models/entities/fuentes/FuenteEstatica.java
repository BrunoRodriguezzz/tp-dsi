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
        // TODO: Se deben reemplazar los print por otra excepcion a catchear por quien lo llama/capa superior
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de lectura: " + e.getMessage());
            return null;
        } catch (CsvException e) {
            JOptionPane.showMessageDialog(null, "Error en CSV de formato: " + e.getMessage());
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
            return null;
        }
    }

    private List<Hecho> instanciarHechosSegunCSV(CSVReader lector) throws IOException, CsvException {
        List<String[]> filas = lector.readAll();
        List<Hecho> listaDeHechos = this.crearListaDeHechosSegunFilas(filas);
        return listaDeHechos;
    }

    private List<Hecho> crearListaDeHechosSegunFilas(List<String[]> filas) {
        List<Hecho> listaDeHechos = new java.util.ArrayList<>();
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
                            parsearFechaALocalDate(campos[5]),
                            Origen.DATASET
                    );
                    listaDeHechos.add(hecho);
                } catch (UbicacionInvalidaException e) {
                    // TODO: Se deben reemplazar los print por otra excepcion a catchear por quien lo llama/capa superior
                    System.err.println("Ubicación inválida para fila: " + Arrays.toString(campos));
                }

                catch (CategoriaInvalidaException e) {
                    // TODO: Se deben reemplazar los print por otra excepcion a catchear por quien lo llama/capa superior
                    System.err.println("Categoria inválida para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (TituloInvalidoException e) {
                    // TODO: Se deben reemplazar los print por otra excepcion a catchear por quien lo llama/capa superior
                    System.err.println("Titulo inválido para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (DescripcionInvalidaException e) {
                    // TODO: Se deben reemplazar los print por otra excepcion a catchear por quien lo llama/capa superior
                    System.err.println("Descripcion inválido para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (FechaInvalidaException e) {
                    // TODO: Se deben reemplazar los print por otra excepcion a catchear por quien lo llama/capa superior
                    System.err.println("Fecha inválida para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }


            }
        }
        return listaDeHechos;
    }

    private static LocalDate parsearFechaALocalDate(String fechaStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(fechaStr, formatter);
        } catch (Exception e) {
            // TODO: Se deben reemplazar los print por otra excepcion a catchear por quien lo llama/capa superior
            JOptionPane.showMessageDialog(null, "Error en formato de fecha: " + fechaStr);
            return null;
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



}