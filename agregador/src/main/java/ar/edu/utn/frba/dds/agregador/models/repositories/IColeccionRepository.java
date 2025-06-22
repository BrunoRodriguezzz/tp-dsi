package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.List;


public interface IColeccionRepository {
  public List<Coleccion> buscarColecciones();
  public Coleccion buscarColeccion(Long id);
  public Coleccion guardarColeccion(Coleccion coleccion);
  public Boolean guardarColecciones(List<Coleccion> colecciones);
  public Boolean eliminarHechoDeColecciones(Hecho hecho);
  public List<Coleccion> buscarCopiaColecciones();
  public Coleccion buscarCopiaColeccion(Long id);
}
