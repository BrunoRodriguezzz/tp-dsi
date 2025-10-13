package ar.edu.utn.frba.dds.servicioEstadisticas.services;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.ColeccionInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.HechoInputDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos.SolicitudEliminacionInputDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IImportadorService {
    List<HechoInputDTO> importarHechos();
    List<ColeccionInputDTO> importarColecciones();
    public List<SolicitudEliminacionInputDTO> importarSolicitudes();
}
