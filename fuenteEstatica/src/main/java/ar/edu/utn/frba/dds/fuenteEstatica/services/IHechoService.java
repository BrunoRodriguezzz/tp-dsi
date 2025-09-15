package ar.edu.utn.frba.dds.fuenteEstatica.services;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.input.InputHechoDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;

import java.util.List;

public interface IHechoService {
    List<ArchivoOutputDTO> getAll();
    public List<ArchivoOutputDTO> getWithFilters(FiltroEstatica filtro);
    ArchivoOutputDTO getById(Long id);
    void save(HechoEstatica hecho);
    void deleteHecho(Long id);
    //public List<ArchivoOutputDTO> getByTitleAndIdFuente(String title, Long idFuente);
}