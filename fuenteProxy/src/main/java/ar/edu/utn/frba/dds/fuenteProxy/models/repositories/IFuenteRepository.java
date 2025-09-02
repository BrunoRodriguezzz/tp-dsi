package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFuenteRepository extends JpaRepository<Fuente, Long> {
//    public List<Fuente> getAll();
//    public Fuente getById(Long id);
//    public void save(Fuente fuente);
//    public void delete(Fuente fuente);
//    public List<Long> devolverIDs();
}
