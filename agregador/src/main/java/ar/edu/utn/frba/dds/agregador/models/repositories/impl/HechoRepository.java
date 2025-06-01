package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOHecho;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOHecho;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class HechoRepository implements IHechoRepository {
  private IDAOHecho dao;
  private final AtomicLong generadorIds = new AtomicLong(1);

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
    Hecho hechoExistente = this.dao.findExistent(hecho);
    if(hechoExistente != null){
      hecho.setId(hechoExistente.getId());
    }
    else{
      hecho.setId(generadorIds.getAndIncrement());
    }
    Hecho hechoGuardado = this.dao.save(hecho);
    return hechoGuardado;
  }

  public List<Hecho> guardarHechos(List<Hecho> hechos) {
    List <Hecho> hechosNuevos = new ArrayList<>();
    hechos.forEach(h -> {
      Hecho hechoGuardado = this.guardarHecho(h);
      hechosNuevos.add(hechoGuardado);
    });
    return hechosNuevos;
  }
}
