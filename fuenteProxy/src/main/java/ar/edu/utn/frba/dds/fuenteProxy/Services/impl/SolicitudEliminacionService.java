package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.ISolicitudEliminacionService;
import ar.edu.utn.frba.dds.fuenteProxy.Services.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.SolicitudEliminacion;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.enums.EstadoSolicitud;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.ISolicitudEliminacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService{

    private final ISolicitudEliminacionRepository solicitudEliminacionRepository;
    private final DetectorDeSpam detectorDeSpam;

    public SolicitudEliminacion crearSolicitud(String hechoId, String motivo, String solicitante) {
        EstadoSolicitud estado =
                        detectorDeSpam.esSpam(motivo) ?
                        EstadoSolicitud.RECHAZADA_SPAM :
                        EstadoSolicitud.PENDIENTE;

        SolicitudEliminacion solicitud = SolicitudEliminacion.builder()
                .hechoId(hechoId)
                .motivo(motivo)
                .solicitante(solicitante)
                .fechaCreacion(LocalDateTime.now())
                .estado(estado)
                .build();

        return solicitudEliminacionRepository.save(solicitud);
    }

    public List<SolicitudEliminacion> obtenerTodas() {
        return solicitudEliminacionRepository.findAll();
    }

    public SolicitudEliminacion aprobar(Long id) {
        return cambiarEstado(id, EstadoSolicitud.APROBADA);
    }

    public SolicitudEliminacion rechazar(Long id) {
        return cambiarEstado(id, EstadoSolicitud.RECHAZADA);
    }

    private SolicitudEliminacion cambiarEstado(Long id, EstadoSolicitud nuevoEstado) {
        SolicitudEliminacion solicitud = solicitudEliminacionRepository.findById(id).orElseThrow();
        solicitud.setEstado(nuevoEstado);
        return solicitudEliminacionRepository.save(solicitud);
    }
}
