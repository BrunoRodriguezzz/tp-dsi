package ar.edu.utn.frba.dds.fuenteProxy.seeder;

import ar.edu.utn.frba.dds.fuenteProxy.Services.impl.HechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.APICatedra;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class HechoSeeder implements CommandLineRunner {
    private final APICatedra apiCatedra;
    private final HechoService hechoService;

    public HechoSeeder(APICatedra apiCatedra, HechoService hechoService) {
        this.apiCatedra = apiCatedra;
        this.hechoService = hechoService;
    }

    @Override
    public void run(String... args) throws Exception {
        apiCatedra.getAll()
                .doOnNext(hechoDTO -> {
                    hechoService.guardarHecho(hechoDTO);
                })
                .blockLast(); // Para que espere a que termine
    }
}
