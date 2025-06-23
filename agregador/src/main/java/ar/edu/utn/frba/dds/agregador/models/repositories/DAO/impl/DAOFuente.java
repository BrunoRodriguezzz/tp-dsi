package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOFuente;

import java.util.ArrayList;
import java.util.List;

public class DAOFuente implements IDAOFuente {
  private List<Fuente> fuentes;

  public DAOFuente() {
    this.fuentes = new ArrayList<>();
  }

  @Override
  public Fuente save(Fuente fuente) {
    Fuente existente = this.findById(fuente.getId());
    if(existente == null) { // No estaba
      this.fuentes.add(fuente);
    }
    else { // Tengo que actualizar -> Siempre llega con el ID correcto
      this.fuentes.remove(existente);
      this.fuentes.add(fuente);
    }
    return fuente;
  }

  @Override
  public Boolean saveAll(List<Fuente> fuentes) {
    fuentes.forEach(this::save);
    return true;
  }

  @Override
  public Fuente findById(Long id) {
    return this.fuentes.stream()
            .filter(f -> f.getId().equals(id))
            .findFirst()
            .orElse(null);
  }

  @Override
  public Fuente findExistent(Fuente fuente) {
      return this.fuentes.stream()
              .filter(f -> f.equals(fuente))
              .findFirst()
              .orElse(null);
  }

  @Override
  public Fuente findByName(String name) {
    return this.fuentes.stream()
            .filter(f -> f.getNombre().equals(name))
            .findFirst()
            .orElse(null);
  }
}
