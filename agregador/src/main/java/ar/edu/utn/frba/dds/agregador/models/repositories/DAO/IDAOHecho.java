package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.List;

public interface IDAOHecho {
  public Hecho save(Hecho hecho);
  public Boolean saveAll(List<Hecho> hechos);
  public Hecho findById(String id);
}
