package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOColeccion;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOColeccion;
import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
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
  public Coleccion buscarColeccion(Long id) {
    return this.dao.find(id);
  }

  @Override
  public List<Coleccion> buscarCopiaColecciones() {
    List<Coleccion> colecciones = this.dao.getAll();
    List<Coleccion> copia = colecciones.stream().map(c -> {
      Coleccion copy = new Coleccion(c.getTitulo(), c.getDescripcion());
      List<Hecho> hechosCopia = new ArrayList<>();
      c.getHechos().forEach(h -> {hechosCopia.add(h);});
      copy.setHechos(hechosCopia);
      copy.setFuentes(c.getFuentes());
      copy.setCriterio(c.getCriterio());
      copy.setId(c.getId());
      return copy;
    }).collect(Collectors.toList());
    return copia;
  }

  @Override
  public Coleccion buscarCopiaColeccion(Long id) {
    Coleccion coleccion = this.dao.find(id);
    Coleccion copia = new Coleccion(coleccion.getTitulo(), coleccion.getDescripcion());
    copia.setFuentes(coleccion.getFuentes());
    copia.setId(coleccion.getId());
    copia.setCriterio(coleccion.getCriterio());
    copia.setHechos(coleccion.getHechos());
    return copia;
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
  public Boolean eliminarHechoDeColecciones(Hecho hecho){
    return this.dao.eliminarHechoDeColecciones(hecho.getId());
  }

}
