package ar.edu.utn.frba.dds.agregador.repositories;

import ar.edu.utn.frba.dds.agregador.repositories.DAO.impl.DaoColeccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.List;


public interface IColeccionRepository {
  public List<Coleccion> buscarColecciones();
  public Coleccion guardarColeccion(Coleccion coleccion);
}
