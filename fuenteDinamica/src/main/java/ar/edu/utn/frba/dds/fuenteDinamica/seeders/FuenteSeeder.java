package ar.edu.utn.frba.dds.fuenteDinamica.seeders;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.FuenteOutputDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Order(1)
public class FuenteSeeder implements CommandLineRunner {

    @Value("${servicio.dinamica}")
    private String urlDinamica;

    @Value("${servicio.agregador}")
    private String urlAgregador;

    @Override
    public void run(String... args) {
        FuenteOutputDTO fuente = new FuenteOutputDTO();
        fuente.setNombre("Provistos por contribuyentes");
        fuente.setTipoFuente("DINAMICA");
        fuente.setPath(urlDinamica);
        fuente.setIdInterno(0L);

        WebClient webClient = WebClient.builder()
            .baseUrl(urlAgregador)
            .build();

        webClient.post()
            .uri(uriBuilder -> uriBuilder.path("/fuentes").build())
            .bodyValue(fuente)
            .retrieve()
            .toBodilessEntity()
            .subscribe();
    }
}
