package ar.edu.utn.frba.dds.fuenteProxy.seeder;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IFuenteService;
import ar.edu.utn.frba.dds.fuenteProxy.Services.impl.HechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.APICatedra;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl.InstanciaMetaMapa;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class FuenteSeeder implements CommandLineRunner {
    private final IFuenteService fuenteService;
    private final APICatedra apiCatedra;
    private final InstanciaMetaMapa apiMetaMapa;

    public FuenteSeeder(IFuenteService fuenteService, APICatedra apiCatedra, InstanciaMetaMapa apiMetaMapa) {
        this.fuenteService = fuenteService;
        this.apiCatedra = apiCatedra;
        this.apiMetaMapa = apiMetaMapa;
    }

    @Override
    public void run(String... args) {
        Fuente fuenteAPICatedra = new Fuente(apiCatedra, "Desastres Naturales", "https://api-ddsi.disilab.ar/public");
        fuenteAPICatedra.setTipoFuente(apiCatedra);
        fuenteAPICatedra.setNombre("Desastres Naturales");
        fuenteService.guardarFuente(fuenteAPICatedra);

        //TODO: No tenemos ruta de otra instancia por ahora
//        Fuente fuenteMetaMapa1 = new Fuente(apiMetaMapa, "Instancia MetaMapa 1", "https://???");
//        fuenteMetaMapa1.setTipoFuente(apiMetaMapa);
//        fuenteMetaMapa1.setNombre("Instancia MetaMapa 1");
//        fuenteService.guardarFuente(fuenteMetaMapa1);
    }
}
