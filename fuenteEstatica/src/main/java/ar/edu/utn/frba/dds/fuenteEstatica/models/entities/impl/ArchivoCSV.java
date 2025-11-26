package ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.enums.Origen;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.TipoArchivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Ubicacion;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.UbicacionInvalidaException;
import com.opencsv.CSVReader;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
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

        if (!filas.isEmpty() && esEncabezado(filas.get(0))) {
            filas = filas.subList(1, filas.size());
        }

        return this.crearListaDeHechosSegunFilas(filas);
    }

    private boolean esEncabezado(String[] fila) {
        if (fila.length < 6) {
            return false;
        }

        try {
            Double.parseDouble(fila[3]);
            Double.parseDouble(fila[4]);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
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
        // 1. Buscamos el archivo dentro del JAR (classpath)
        ClassPathResource resource = new ClassPathResource(ruta);

        // 2. Verificamos si existe antes de intentar abrirlo
        if (!resource.exists()) {
            throw new IOException("No se encontró el archivo en el classpath (resources): " + ruta);
        }

        // 3. Usamos InputStreamReader en lugar de FileReader
        // Esto funciona tanto en tu IDE local como dentro del Docker en Railway
        return new CSVReaderBuilder(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                .withSkipLines(0)
                .withCSVParser(new com.opencsv.CSVParserBuilder()
                        .withSeparator(',')
                        .withQuoteChar('\"')
                        .build())
                .build();
    }
}
