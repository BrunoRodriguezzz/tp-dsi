package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IImportadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ImportadorService implements IImportadorService {
    private final ClienteAgregador clienteAgregador;

    @Autowired
    public ImportadorService(ClienteAgregador clienteAgregador) {
        this.clienteAgregador = clienteAgregador;
    }

    @Override
    public List<HechoInputDTO> importarHechos() {
        return this.clienteAgregador.generarHechosIndependientes();
    }

    @Override
    public List<ColeccionInputDTO> importarColecciones() {
        return this.clienteAgregador.generarColecciones();
    }

    @Override
    public List<SolicitudEliminacionInputDTO> importarSolicitudes() {
        return this.clienteAgregador.generarSolicitudesInputDTO();
    }
}
