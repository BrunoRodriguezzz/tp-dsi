package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import java.util.List;

public interface IDAOHecho {
  public Hecho save(Hecho hecho);
  public Boolean saveAll(List<Hecho> hechos);
  public Hecho findById(Long id);
  public List <Hecho> findAll();
  public Hecho findExistent(Hecho hecho);
  public List<Hecho> findByFuente(Fuente fuente);
}
