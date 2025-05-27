package ar.edu.utn.frba.dds.fuenteProxy.controllers;

import ar.edu.utn.frba.dds.fuenteProxy.Services.ISolicitudEliminacionService;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;

public class SolicitudController {
    private ISolicitudEliminacionService solicitudEliminacionService;

    public void send(SolicitudEliminacionDTO solicitud) {
        solicitudEliminacionService.send(solicitud);
    }
}
