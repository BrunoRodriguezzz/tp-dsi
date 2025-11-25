package ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.enums.Origen;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.TipoArchivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Ubicacion;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.UbicacionInvalidaException;
import com.opencsv.CSVReader;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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
        } catch (IOException e) {
            throw new RuntimeException("Error de lectura: " + e.getMessage());
        } catch (CsvException e) {
            throw new RuntimeException("Error en CSV de formato: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
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
                    hecho.setEliminado(false);
                    return hecho;
                });
    }

    private static LocalDateTime parsearFechaALocalDate(String fechaStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fAux = LocalDate.parse(fechaStr, formatter);
        return fAux.atStartOfDay();
    }

    private CSVReader crearLectorCSV(String ruta) throws IOException {
        java.io.File file = new java.io.File(ruta);

        if (!file.isAbsolute()) {
            java.nio.file.Path currentPath = java.nio.file.Paths.get("").toAbsolutePath();
            java.nio.file.Path absolutePath = currentPath.getParent().resolve(ruta);

            if (java.nio.file.Files.exists(absolutePath)) {
                file = absolutePath.toFile();
            }
        }

        return new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1) // Saltar encabezados
                .withCSVParser(new com.opencsv.CSVParserBuilder()
                        .withSeparator(',')
                        .withQuoteChar('\"')
                        .build())
                .build();
    }
}
