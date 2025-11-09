package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IImportadorService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ImportadorService implements IImportadorService {

    private final WebClient webClient;

    public ImportadorService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    @Override
    public List<HechoInputDTO> importarHechos() {
        return GeneradorDatosPrueba.generarHechosIndependientes();
    }

    @Override
    public List<ColeccionInputDTO> importarColecciones() {
        return GeneradorDatosPrueba.generarColecciones();
    }

    @Override
    public List<SolicitudEliminacionInputDTO> importarSolicitudes() {
        return GeneradorDatosPrueba.generarSolicitudesInputDTO();
    }
}
