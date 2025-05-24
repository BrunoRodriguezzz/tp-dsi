package ar.edu.utn.frba.dds.fuenteProxy.models.domain;

import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.SolicitudEliminacionDTO;

import java.util.List;

public interface TipoFuente {
    public List<HechoDTO> getAll();
    public HechoDTO getById(Integer id);
    public void informarSolicitudEliminaicion(SolicitudEliminacionDTO sol);
}
