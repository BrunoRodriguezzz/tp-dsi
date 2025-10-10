package ar.edu.utn.frba.dds.fuenteEstatica.services;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;

import java.util.List;

public interface IHechoService {
    // Obtener hechos
    List<ArchivoOutputDTO> getWithFilters(FiltroEstatica filtro);
    ArchivoOutputDTO getHechoById(Long id);
    ArchivoOutputDTO getByFuenteId(Long id, FiltroEstatica filtroEstatica);

    // Operaciones sobre los hechos
    void save(HechoEstatica hecho);
    void deleteHecho(Long id);
}