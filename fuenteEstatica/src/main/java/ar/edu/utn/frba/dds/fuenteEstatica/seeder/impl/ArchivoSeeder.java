package ar.edu.utn.frba.dds.fuenteEstatica.seeder.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.impl.ArchivoCSV;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ArchivoSeeder implements CommandLineRunner {
    private final ArchivoCSV archivoCSV;
    private final IArchivoRepository archivoRepository;
    @Value("${path.csv}")
    private String rutaArchivo;
    @Value("${nombre.csv}")
    private String nombreArchivo;

    public ArchivoSeeder(IArchivoRepository archivoRepository, ArchivoCSV archivoCSV) {
        this.archivoRepository = archivoRepository;
        this.archivoCSV = archivoCSV;

    }

    @Override
    public void run(String... args) throws Exception {
        Archivo archivo = new Archivo();

        archivo.setNombre(this.nombreArchivo);
        archivo.setRutaArchivo(this.rutaArchivo);
        archivo.setTipoArchivo(this.archivoCSV);

        this.archivoRepository.save(archivo);

    }
}
