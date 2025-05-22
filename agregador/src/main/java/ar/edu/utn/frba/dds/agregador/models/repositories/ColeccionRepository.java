package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDaoColeccion;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DaoColeccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ColeccionRepository implements IColeccionRepository {
  private IDaoColeccion dao;

  public ColeccionRepository() {
    this.dao = new DaoColeccion();
  }

  @Override
  public List<Coleccion> buscarColecciones() {
    List<Coleccion> colecciones = this.dao.getAll();
    return colecciones;
  }

  @Override
  public Coleccion guardarColeccion(Coleccion coleccion){
    return this.dao.save(coleccion);
  }

  @Override
  public Boolean guardarColecciones(List<Coleccion> colecciones){
    return this.dao.save(colecciones);
  }

}
