package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOColeccion;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOColeccion;
import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ColeccionRepository implements IColeccionRepository {
  private IDAOColeccion dao;
  private final AtomicLong generadorIds = new AtomicLong(1);

  public ColeccionRepository() {
    this.dao = new DAOColeccion();
  }

  @Override
  public List<Coleccion> buscarColecciones() {
    List<Coleccion> colecciones = this.dao.getAll();
    return colecciones;
  }

  @Override
  public Coleccion guardarColeccion(Coleccion coleccion){
    if(coleccion.getId() == null){
      coleccion.setId(generadorIds.getAndIncrement());
    }
    return this.dao.save(coleccion);
  }

  @Override
  public Boolean guardarColecciones(List<Coleccion> colecciones){
    return this.dao.save(colecciones);
  }

  @Override
  public Coleccion buscarColeccion(String id) {
    return this.dao.find(id);
  }
}
