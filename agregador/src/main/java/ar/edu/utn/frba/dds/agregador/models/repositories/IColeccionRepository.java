package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.List;


public interface IColeccionRepository {
  public List<Coleccion> buscarColecciones();
  public Coleccion buscarColeccion(String id);
  public Coleccion guardarColeccion(Coleccion coleccion);
  public Boolean guardarColecciones(List<Coleccion> colecciones);
}
