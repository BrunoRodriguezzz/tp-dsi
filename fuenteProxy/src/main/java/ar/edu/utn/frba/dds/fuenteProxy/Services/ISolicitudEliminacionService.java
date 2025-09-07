package ar.edu.utn.frba.dds.fuenteProxy.Services;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.SolicitudEliminacion;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;

import java.util.List;

public interface ISolicitudEliminacionService {

    SolicitudEliminacion crearSolicitud(String hechoId, String motivo, String solicitante);

    List<SolicitudEliminacion> obtenerTodas();

    SolicitudEliminacion aprobar(Long id);

    SolicitudEliminacion rechazar(Long id);
}
