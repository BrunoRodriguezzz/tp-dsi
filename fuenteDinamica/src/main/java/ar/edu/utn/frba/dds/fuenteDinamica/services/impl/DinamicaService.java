package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IDinamicaService;

import java.util.List;

public class DinamicaService implements IDinamicaService {

    private IDinamicaRepository dinamicaRepository;

    @Override
    public List<HechoOutputDTO> buscarHechos() {
        return List.of();
    }

    @Override
    public HechoOutputDTO buscarPorID(Long id) {
        return null;
    }

    @Override
    public HechoOutputDTO crear(HechoInputDTO hechoInputDTO) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho){
        //TODO: transformar un Hecho cualquier a tipo OutputDTO
        return null;
    }
}