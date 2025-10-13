package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.FuenteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuenteService {
    private final MockService mockService;

    public FuenteService() {
        this.mockService = new MockService();
    }

    public List<FuenteDTO> obtenerFuentesNuevas() {
        return this.mockService.obtenerFuentesMockeadas();
    }
}
