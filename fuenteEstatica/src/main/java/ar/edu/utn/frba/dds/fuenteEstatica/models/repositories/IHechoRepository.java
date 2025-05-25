package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.HechoDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;

import java.util.List;

public interface IHechoRepository {
    List<HechoEstatica> findAll();
}
