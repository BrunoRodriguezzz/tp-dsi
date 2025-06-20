package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;

import java.util.List;

public interface IArchivoRepository {
    public List<Archivo> getAll();
    public void save(Archivo archivo);
    public List<Long> devolverIDs();
    public Archivo getById(Long id);
}
