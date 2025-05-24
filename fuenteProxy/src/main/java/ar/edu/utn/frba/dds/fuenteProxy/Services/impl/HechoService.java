package ar.edu.utn.frba.dds.fuenteProxy.Services.impl;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IHechoService;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IFuenteRepository;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;

import java.util.Date;
import java.util.List;

public class HechoService implements IHechoService {
    private IFuenteRepository fuentes; //TODO Podría implementar un DAO para cada uno.
    private IHechoRepository hechos;

    @Override
    public List<HechoDTO> getAll() { //TODO
        return List.of();
    }

    @Override
    public List<HechoDTO> getNew(Date fecha) { //TODO
        return List.of();
    }

    @Override
    public HechoDTO getById(Long id) { //TODO
        return null;
    }

    @Override
    public List<HechoDTO> getAllFuente(Long fuenteId) { //TODO
        return List.of();
    }

    @Override
    public void delete(Long idHecho) { //TODO

    }
}
