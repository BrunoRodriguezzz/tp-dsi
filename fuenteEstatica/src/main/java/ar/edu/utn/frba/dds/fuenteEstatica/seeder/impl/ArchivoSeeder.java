package ar.edu.utn.frba.dds.fuenteEstatica.seeder.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl.ArchivoCSV;
import ar.edu.utn.frba.dds.fuenteEstatica.services.IArchivoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@Order(1)
public class ArchivoSeeder implements CommandLineRunner {
    private final ArchivoCSV archivoCSV;
    private final IArchivoService archivoService;
    @Value("${path.csv}")
    private String rutaArchivo;
    @Value("${nombre.csv}")
    private String nombreArchivo;

    public ArchivoSeeder(IArchivoService archivoService, ArchivoCSV archivoCSV) {
        this.archivoService = archivoService;
        this.archivoCSV = archivoCSV;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Archivo archivo = new Archivo();
            archivo.setNombre(this.nombreArchivo);
            archivo.setRutaArchivo(this.rutaArchivo);
            archivo.setTipoArchivo(this.archivoCSV);
            archivo.setUltimaConsulta(LocalDateTime.now());

            this.archivoService.guardarArchivoSync(archivo);
        } catch (Exception e) {
            log.error("Error al cargar archivo inicial '{}': {}", nombreArchivo, e.getMessage(), e);
            throw e;
        }
    }
}
