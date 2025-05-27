package ar.edu.utn.frba.dds.fuenteProxy.Services;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;

public interface ISolicitudEliminacionService {
    public void send(SolicitudEliminacionDTO solicitud);
}
