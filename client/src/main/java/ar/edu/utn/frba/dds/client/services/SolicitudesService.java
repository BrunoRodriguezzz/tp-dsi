package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.solicitud.SolicitudDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudesService {
    private final MockService mockService;

    public SolicitudesService() {
        this.mockService = new MockService();
    }

    public List<SolicitudDTO> obtenerSolicitudes() {
        return this.mockService.obtenerSolicitudesMockeadas();
    }
}
