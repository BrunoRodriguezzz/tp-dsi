package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOHecho;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;

public class DAOHecho implements IDAOHecho {
  private List<Hecho> hechos;

  public DAOHecho() {this.hechos = new ArrayList<Hecho>();}

  public Hecho save(Hecho hecho) {
    this.hechos.add(hecho);
    return hecho;
  }

  public Boolean saveAll(List<Hecho> hechos) {
    this.hechos.addAll(hechos);
    return true;
  }

  public Hecho findById(String id) {
    Hecho hecho = this.hechos.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
    return hecho;
  }
}
