package ar.edu.utn.frba.dds.agregador.schedulers;

import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefrescoScheduler {
  private final IColeccionService coleccionService;

  public RefrescoScheduler(IColeccionService coleccionService) {
    this.coleccionService = coleccionService;
  }

  @Scheduled(cron = "0 0 * * * *")
  public void refrescarColecciones() {
    this.coleccionService.refrescarColecciones();
  }

}
