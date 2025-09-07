package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<HechoProxy, Long>, JpaSpecificationExecutor<HechoProxy> {
    List<HechoProxy> findByIdFuente(Long id);

    List<HechoProxy> findByIdFuenteAndIdExterno(Long idFuente, Long idExterno);
//    public List<HechoProxy> getAll();
//    public List<HechoProxy> getWithFilters(FiltroProxy filtro);
//    public void delete(Long idHecho);
//    public List<HechoProxy> getByIdFuente(Long idFuente);
//    public HechoProxy getById(Long id);
//    public void guardarHecho(HechoProxy hecho);
//    public List<HechoProxy> getFiltrados(Long idFuente, FiltroProxy filtro);
}
