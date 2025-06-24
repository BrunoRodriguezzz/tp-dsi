package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;

import ar.edu.utn.frba.dds.agregador.models.domain.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import java.util.List;

public interface IDAOFuente {
  public Fuente save(Fuente fuente);
  public Boolean saveAll(List<Fuente> fuentes);
  public Fuente findById(Long id);
  public Fuente findExistent(Fuente fuente);
  public Fuente findByName(String name);
  public List<Fuente> findAll();
}
