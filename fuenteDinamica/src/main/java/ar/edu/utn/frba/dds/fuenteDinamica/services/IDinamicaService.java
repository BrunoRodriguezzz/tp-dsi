package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IDinamicaService {

    public List<HechoOutputDTO> buscarHechos();
    public HechoOutputDTO       buscarPorID(Long id);
    public HechoOutputDTO       crear(HechoInputDTO hechoInputDTO);
    public void                 eliminar(Long id);

}
