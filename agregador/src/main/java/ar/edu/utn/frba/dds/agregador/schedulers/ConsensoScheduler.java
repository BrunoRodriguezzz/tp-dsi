package ar.edu.utn.frba.dds.agregador.schedulers;

import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConsensoScheduler {
  private final IHechoService hechoService;

  public ConsensoScheduler(IHechoService hechoService) {
    this.hechoService = hechoService;
  }

  @Scheduled(cron = "0 0 4 * * *")
  public void consensuarHechos() {
    this.hechoService.consensuarHechos();
  }

}
