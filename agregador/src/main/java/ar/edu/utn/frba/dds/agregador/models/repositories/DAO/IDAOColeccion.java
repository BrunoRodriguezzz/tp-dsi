package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;
import ar.edu.utn.frba.dds.agregador.models.domain.Coleccion;
import java.util.List;

public interface IDAOColeccion {
  public List<Coleccion> getAll();
  public Coleccion save(Coleccion coleccion);
  public Boolean save(List<Coleccion> colecciones);
  public Coleccion find(Long id);
  public Boolean eliminarHechoDeColecciones(Long id);
}
