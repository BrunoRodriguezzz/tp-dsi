package ar.edu.utn.frba.dds.agregador.repositories.DAO;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.List;

public interface IDaoColeccion {
  public List<Coleccion> getAll();
  public Coleccion save(Coleccion coleccion);
}
