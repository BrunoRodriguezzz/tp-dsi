package ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.UbicacionInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Ubicacion;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.TipoArchivo;
import com.opencsv.CSVReader;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ArchivoCSV implements TipoArchivo {
    @Override
    public Flux<HechoEstatica> importarHechos(String ruta) {
       return this.leerHechos(ruta);
    }

    public Flux<HechoEstatica> leerHechos(String ruta) {
        try (CSVReader lector = this.crearLectorCSV(ruta)) {
            return this.instanciarHechosSegunCSV(lector);
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

    private Flux<HechoEstatica> instanciarHechosSegunCSV(CSVReader lector) throws IOException, CsvException {
        List<String[]> filas = lector.readAll();

        return this.crearListaDeHechosSegunFilas(filas);
    }

    private Flux<HechoEstatica> crearListaDeHechosSegunFilas(List<String[]> filas) {
        return Flux.fromIterable(filas)
                .filter(campos -> campos.length >= 6)
                .map(campos -> {
                    HechoEstatica hecho = new HechoEstatica();
                    try {
                        Ubicacion ubicacion = new Ubicacion(campos[3], campos[4]);
                        hecho.setUbicacion(ubicacion);
                    } catch (UbicacionInvalidaException e) {
                        throw new RuntimeException(e);
                    }
                    hecho.setTitulo(campos[0]);
                    hecho.setDescripcion(campos[1]);
                    hecho.setCategoria(campos[2]);
                    hecho.setFechaHecho(this.parsearFechaALocalDate(campos[5]));
                    hecho.setFechaCreacion(LocalDateTime.now());
                    hecho.setFechaModificacion(LocalDateTime.now());
                    hecho.setOrigen(Origen.DATASET);
                    return hecho;
                });
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

    private CSVReader crearLectorCSV(String ruta) throws IOException {
        // Saltar encabezados
        return new CSVReaderBuilder(new FileReader(ruta))
                .withSkipLines(1) // Saltar encabezados
                .withCSVParser(new com.opencsv.CSVParserBuilder()
                        .withSeparator(',')
                        .withQuoteChar('\"')
                        .build())
                .build();
    }
}
