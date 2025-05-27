package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;

import java.util.List;

public interface IArchivoRepository {
    List<Archivo> getAll();
}
