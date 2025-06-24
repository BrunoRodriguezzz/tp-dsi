package ar.edu.utn.frba.dds.fuenteProxy.seeder;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IFuenteService;
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
    private final IFuenteService fuenteService;
    private final APICatedra apiCatedra;

    public FuenteSeeder(IFuenteService fuenteService, APICatedra apiCatedra) {
        this.fuenteService = fuenteService;
        this.apiCatedra = apiCatedra;
    }

    @Override
    public void run(String... args) {
        Fuente fuenteAPICatedra = new Fuente(apiCatedra, "Desastres Naturales", "https://api-ddsi.disilab.ar/public");
        fuenteAPICatedra.setTipoFuente(apiCatedra);
        fuenteAPICatedra.setNombre("Desastres Naturales");
        fuenteService.guardarFuente(fuenteAPICatedra);
    }
}
