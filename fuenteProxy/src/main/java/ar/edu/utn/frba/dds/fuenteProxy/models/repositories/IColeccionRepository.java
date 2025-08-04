package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Coleccion;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;

import java.util.List;

public interface IColeccionRepository {
    List<Coleccion> getAll();
    Coleccion getById(Long id);
    void guardarColeccion(Coleccion coleccion);
}
