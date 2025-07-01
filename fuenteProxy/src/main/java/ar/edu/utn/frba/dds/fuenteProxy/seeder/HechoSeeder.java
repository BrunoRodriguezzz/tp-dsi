package ar.edu.utn.frba.dds.fuenteProxy.seeder;

import ar.edu.utn.frba.dds.fuenteProxy.Services.impl.HechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.APICatedra;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class HechoSeeder implements CommandLineRunner {
    private final HechoService hechoService;
    private final IFuenteRepository fuenteRepository;

    public HechoSeeder(HechoService hechoService, IFuenteRepository fuenteRepository) {
        this.hechoService = hechoService;
        this.fuenteRepository = fuenteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        fuenteRepository.getAll().forEach(f -> {
            f.getAllHechos()
                    .doOnNext(hechoService::guardarHecho)
                    .blockLast(); // Para que espere a que termine
        });
    }
}
