package ar.edu.utn.frba.dds.fuenteEstatica.services;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.HechoDTO;

import java.util.List;

public interface IHechoService {
    public List<HechoDTO> findAll();
}
