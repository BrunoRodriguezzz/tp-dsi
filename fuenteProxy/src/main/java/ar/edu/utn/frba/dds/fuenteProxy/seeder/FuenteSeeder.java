package ar.edu.utn.frba.dds.fuenteProxy.seeder;

import ar.edu.utn.frba.dds.fuenteProxy.Services.impl.HechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.APICatedra;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class FuenteSeeder implements CommandLineRunner {
    private final HechoService hechoService;
    private IFuenteRepository fuentesRepository;
    private final APICatedra apiCatedra;

    public FuenteSeeder(HechoService hechoService, APICatedra apiCatedra, IFuenteRepository fuentesRepository) {
        this.hechoService = hechoService;
        this.apiCatedra = apiCatedra;
        this.fuentesRepository = fuentesRepository;
    }

    @Override
    public void run(String... args) {
        Fuente fuenteAPICatedra = new Fuente(apiCatedra, "Desastres Naturales", "https://api-ddsi.disilab.ar/public");
        fuenteAPICatedra.setTipoFuente(apiCatedra);
        fuenteAPICatedra.setNombre("Desastres Naturales");
        fuentesRepository.save(fuenteAPICatedra);
    }
}
