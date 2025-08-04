package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOFuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOFuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class FuenteRepository implements IFuenteRepository {
  private IDAOFuente dao;
  private final AtomicLong generadorIds = new AtomicLong(1);

  public FuenteRepository(){
    this.dao = new DAOFuente();
  }

  @Override
  public Fuente guardarFuente(Fuente fuente) {
    Fuente existente = this.dao.findExistent(fuente);
    if (existente != null){ // Ya estaba
      fuente.setId(existente.getId());
    }
    else {
      fuente.setId(generadorIds.getAndIncrement());
    }
    return this.dao.save(fuente);
  }

  @Override
  public Fuente buscarFuente(Long id) {
    return this.dao.findById(id);
  }

  @Override
  public Fuente buscarFuente(String nombre) {
    return this.dao.findByName(nombre);
  }

  @Override
  public Fuente buscarFuenteNombre(String nombre) {
    return this.dao.findByName(nombre);
  }

  @Override
  public List<Fuente> buscarFuentes() {
    List<Fuente> fuentes = this.dao.findAll();
    return fuentes;
  }
}
