package ar.edu.utn.frba.dds.fuenteEstatica.services;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;

import java.util.List;

public interface IHechoService {
    List<ArchivoOutputDTO> getAll();
    public List<ArchivoOutputDTO> getWithFilters(FiltroEstatica filtro);
    HechoOutputDTO getById(Long id);
    HechoOutputDTO crearHecho(HechoEstatica hecho);
    void deleteHecho(Long id);
    public void guardarHecho(HechoEstatica hecho);
}
