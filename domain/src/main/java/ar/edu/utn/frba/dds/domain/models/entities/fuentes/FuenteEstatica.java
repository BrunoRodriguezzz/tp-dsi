package ar.edu.utn.frba.dds.domain.models.entities.fuentes;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.CategoriaInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.DescripcionInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.FechaInvalidaException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.TituloInvalidoException;
import ar.edu.utn.frba.dds.domain.models.entities.utils.Errores.ER_ValueObjects.UbicacionInvalidaException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Ubicacion;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class FuenteEstatica implements Fuente {
    private String rutaArchivo;

    public FuenteEstatica(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public List<Hecho> importarHechos() {
        try (CSVReader lector = this.crearLectorCSV()) {
            List<Hecho> hechos = this.instanciarHechosSegunCSV(lector);
            return hechos;
            // TODO: Catchea el controller?
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
                    // TODO: Catchea el controller?
                    System.err.println("Ubicación inválida para fila: " + Arrays.toString(campos));
                }

                catch (CategoriaInvalidaException e) {
                    // TODO: Catchea el controller?
                    System.err.println("Categoria inválida para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (TituloInvalidoException e) {
                    // TODO: Catchea el controller?
                    System.err.println("Titulo inválido para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (DescripcionInvalidaException e) {
                    // TODO: Catchea el controller?
                    System.err.println("Descripcion inválido para fila: " + Arrays.toString(campos) + ": " + e.getMessage());
                }

                catch (FechaInvalidaException e) {
                    // TODO: Catchea el controller?
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
            // TODO: Catchea el controller?
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