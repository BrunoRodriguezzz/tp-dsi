package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOHecho;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOHecho;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class HechoRepository implements IHechoRepository {
  private IDAOHecho dao;

  public HechoRepository(){
    this.dao = new DAOHecho();
  }

  public Boolean inicializarHechos(List<Hecho> hechos) {
    return this.dao.saveAll(hechos);
  }

  public Hecho buscarHecho(Long id) {
    return this.dao.findById(id);
  }

  public Hecho guardarHecho(Hecho hecho) {
    Hecho hechoGuardado = this.dao.save(hecho);
    return hechoGuardado;
  }
}
