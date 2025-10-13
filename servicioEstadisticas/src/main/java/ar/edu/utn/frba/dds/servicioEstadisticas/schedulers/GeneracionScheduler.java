package ar.edu.utn.frba.dds.servicioEstadisticas.schedulers;

import ar.edu.utn.frba.dds.servicioEstadisticas.services.IEstadisticaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GeneracionScheduler {
  private final IEstadisticaService estadisticaService;

  public GeneracionScheduler(IEstadisticaService estadisticaService) {this.estadisticaService = estadisticaService;}

  @Scheduled(cron = "0 0 3 * * *")
  public void consensuarHechos() {
    this.estadisticaService.calcularEstadisticas();
  }
}
