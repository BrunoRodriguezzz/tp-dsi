package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;

import java.util.List;

public interface IHechoRepository {
    public List<HechoProxy> getAll();
    public List<HechoProxy> getWithFilters(FiltroProxy filtro);
    public void delete(Long idHecho);
    public List<HechoProxy> getByIdFuente(Long idFuente);
    public HechoProxy getById(Long id);
    public void guardarHecho(HechoProxy hecho);
    public List<HechoProxy> getFiltrados(Long idFuente, FiltroProxy filtro);
}
