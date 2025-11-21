package ar.edu.utn.frba.dds.fuenteProxy.seeder;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IFuenteService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.enums.TipoFuenteEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class FuenteSeeder implements CommandLineRunner {
    private final IFuenteService fuenteService;

    public FuenteSeeder(IFuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    public void run(String... args) {
         Fuente fuenteAPICatedra = new Fuente(TipoFuenteEnum.APICATEDRA, "Desastres Naturales", "https://api-ddsi.disilab.ar/public");
         fuenteService.guardarFuente(fuenteAPICatedra);
    }
}
