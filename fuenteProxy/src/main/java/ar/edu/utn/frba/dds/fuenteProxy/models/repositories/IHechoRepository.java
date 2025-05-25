package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;

import java.util.List;

public interface IHechoRepository {
    public List<HechoProxy> getAll();
    public List<HechoProxy> getWithFilters(FiltroProxy filtro);
    public void delete(Long idHecho);
}
