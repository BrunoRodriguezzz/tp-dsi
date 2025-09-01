package ar.edu.utn.frba.dds.fuenteProxy.seeder;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IColeccionService;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class ColeccionesSeeder implements CommandLineRunner {
    private final IFuenteRepository fuenteRepository;
    private final IColeccionService coleccionService;

    public ColeccionesSeeder(IFuenteRepository fuenteRepository, IColeccionService coleccionService) {
        this.coleccionService = coleccionService;
        this.fuenteRepository = fuenteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        fuenteRepository.findAll()
//                .stream()
//                .toList()
//                .forEach(f -> f.getAllColecciones()
//                        .doOnNext(coleccionService::guardarColeccion)
//                        .blockLast());
    }
}
