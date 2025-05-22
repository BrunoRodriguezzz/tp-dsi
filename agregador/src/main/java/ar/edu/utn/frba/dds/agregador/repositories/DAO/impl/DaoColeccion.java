package ar.edu.utn.frba.dds.agregador.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.repositories.DAO.IDaoColeccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.ArrayList;
import java.util.List;

public class DaoColeccion implements IDaoColeccion {
  private List<Coleccion> colecciones;

  public DaoColeccion() {
    this.colecciones = new ArrayList<>();
  }

  public List<Coleccion> getAll(){
    return this.colecciones;
  }

  public Coleccion save(Coleccion coleccion){
    this.colecciones.add(coleccion);
    return coleccion;
  }
}
