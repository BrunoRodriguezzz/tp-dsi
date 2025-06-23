package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOFuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOFuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.IFuenteRepository;
import java.util.ArrayList;
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
    //TODO
    return null;
  }

  @Override
  public Boolean inicializarFuentes(List<Fuente> fuentes) {
    //TODO
    return null;
  }

  @Override
  public Fuente buscarFuente(Long id) {
    //TODO
    return null;
  }

  @Override
  public List<Fuente> guardarFuente(List<Fuente> hechos) {
    //TODO
    return List.of();
  }
}
