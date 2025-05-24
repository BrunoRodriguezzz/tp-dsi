package ar.edu.utn.frba.dds.fuenteProxy.models.domain.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.TipoFuente;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class APICatedra implements TipoFuente {
    private WebClient webClient;

    @Override
    public List<HechoDTO> getAll() {
        //TODO
        return List.of();
    }

    @Override
    public HechoDTO getById(Integer id) {
        //TODO
        return null;
    }

    @Override
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol) {
        //TODO
    }
}
