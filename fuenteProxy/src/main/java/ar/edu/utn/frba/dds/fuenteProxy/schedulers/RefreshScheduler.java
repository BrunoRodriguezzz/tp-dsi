package ar.edu.utn.frba.dds.fuenteProxy.schedulers;

public class RefreshScheduler {
}

//package ar.edu.utn.frba.dds.agregador.schedulers;
//
//import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RefrescoScheduler {
//    private final IAgregadorService agregadorService;
//
//    public RefrescoScheduler(IAgregadorService agregadorService) {
//        this.agregadorService = agregadorService;
//    }
//
//    @Scheduled(cron = "0 0 * * * *")
//    public void refrescarColecciones() {
//        this.agregadorService.refrescarColecciones();
//    }
//
//}