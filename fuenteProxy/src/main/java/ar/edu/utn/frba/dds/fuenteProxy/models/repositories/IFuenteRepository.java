package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;

import java.util.List;

public interface IFuenteRepository {
    public List<Fuente> getAll();
    public Fuente getById(Long id);
    public void save(Fuente fuente);
    public void delete(Fuente fuente);
}
