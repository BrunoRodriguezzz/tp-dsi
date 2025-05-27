package ar.edu.utn.frba.dds.fuenteEstatica.seeder.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.services.impl.HechoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class HechoSeeder implements CommandLineRunner {
    private final HechoService hechoService;
    private final IArchivoRepository archivoRepository;

    public HechoSeeder(HechoService hechoService, IArchivoRepository archivoRepository) {
        this.hechoService = hechoService;
        this.archivoRepository = archivoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        archivoRepository.getAll().forEach(archivo -> {
            archivo.importarHechos()
                    .doOnNext(hechoService::guardarHecho)
                    .blockLast(); // Para que espere a que termine
        });
    }
}
