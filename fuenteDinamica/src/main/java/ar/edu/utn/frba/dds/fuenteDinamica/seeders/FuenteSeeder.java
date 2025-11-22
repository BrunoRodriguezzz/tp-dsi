package ar.edu.utn.frba.dds.fuenteDinamica.seeders;

import ar.edu.utn.frba.dds.fuenteDinamica.clients.AgregadorClient;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.FuenteOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class FuenteSeeder implements CommandLineRunner {

    @Value("${servicio.dinamica}")
    private String urlDinamica;

    private final AgregadorClient agregadorClient;

    @Autowired
    public FuenteSeeder(AgregadorClient agregadorClient) {
        this.agregadorClient = agregadorClient;
    }

    @Override
    @Async
    public void run(String... args) {
        FuenteOutputDTO fuente = new FuenteOutputDTO();
        fuente.setNombre("Provistos por contribuyentes");
        fuente.setTipoFuente("DINAMICA");
        fuente.setPath(urlDinamica);
        fuente.setIdInterno(0L);

        try {
            this.agregadorClient.incorporarFuente(fuente);
        } catch (Exception e) {
            System.out.println("No se pudo comunicar con el Agregador: " + e.getMessage());
        }
    }
}
