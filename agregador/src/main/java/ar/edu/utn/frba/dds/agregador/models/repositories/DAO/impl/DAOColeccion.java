package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOColeccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.ArrayList;
import java.util.List;

public class DAOColeccion implements IDAOColeccion {
  private List<Coleccion> colecciones;

  public DAOColeccion() {
    this.colecciones = new ArrayList<>();
  }

  public List<Coleccion> getAll(){
    return this.colecciones;
  }

  public Coleccion save(Coleccion coleccion) {
    this.colecciones.add(coleccion);
    return coleccion;
  }

  public Boolean save(List<Coleccion> colecciones) {
    this.colecciones = colecciones;
    return true;
  }
}
