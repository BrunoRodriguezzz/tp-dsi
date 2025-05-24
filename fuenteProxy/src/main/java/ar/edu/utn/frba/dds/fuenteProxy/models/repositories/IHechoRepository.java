package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;

import java.util.List;

public interface IHechoRepository {
    public List<HechoProxy> getAll();
    public HechoProxy getById(Long id);
    public void save(HechoProxy hecho);
    public void delete(HechoProxy hecho);
}
