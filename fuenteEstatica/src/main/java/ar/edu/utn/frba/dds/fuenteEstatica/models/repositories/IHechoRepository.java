package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;

import java.util.List;

public interface IHechoRepository {
    List<HechoEstatica> getAll();
    HechoEstatica getById(Long id);
    HechoEstatica save(HechoEstatica hechoEstatica);
    public List<HechoEstatica> getByIdArchivo(Long id);
    void delete(Long id);
}
