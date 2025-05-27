package ar.edu.utn.frba.dds.fuenteEstatica.services;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;

import java.util.List;

public interface IHechoService {
    List<ArchivoOutputDTO> getAll();
    HechoOutputDTO getById(Long id);
    HechoOutputDTO crearHecho(HechoEstatica hecho);
    void deleteHecho(Long id);
}
