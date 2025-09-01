package ar.edu.utn.frba.dds.fuenteProxy.schedulers;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Component
public class RefreshScheduler {
    private final IFuenteRepository fuentes;
    private final IHechoService hechoService;

    public RefreshScheduler(IFuenteRepository fuentesRepository, IHechoService hechoService) {
        this.fuentes = fuentesRepository;
        this.hechoService = hechoService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void refrescarColecciones() {
        LocalDateTime date = LocalDateTime.now().minusHours(1);
        fuentes.findAll().forEach(f -> {
            f.getNuevos(date)
                    .doOnNext(h -> {
                        if (h != null) {
                            hechoService.guardarHecho(h);
                        }
                    })
                    .blockLast(); // Para que espere a que termine
        });
    }
}

