package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOColeccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOColeccion implements IDAOColeccion {
  private List<Coleccion> colecciones;

  public DAOColeccion() {
    this.colecciones = new ArrayList<>();
  }

  public List<Coleccion> getAll(){
    return this.colecciones;
  }

  public Coleccion find(String id) {
    Coleccion coleccion = this.colecciones.stream().filter(c ->
        c.getId().equals(id)).findFirst().orElse(null);
    if(coleccion == null){
      System.out.println("Coleccion no encontrada");
    }
    return coleccion;
  }

  public Boolean save(List<Coleccion> colecciones) {
    this.colecciones = colecciones;
    return true;
  }

  // Solo la usa el Seeder
  public Coleccion save(Coleccion coleccion) {
    Optional<Coleccion> existente = this.colecciones.stream()
        .filter(c -> c.getId().equals(coleccion.getId()))
        .findFirst();

    if (existente.isPresent()) {
      Coleccion coleccionExistente = existente.get();

      coleccionExistente.setId(coleccion.getId());
      coleccionExistente.setDescripcion(coleccion.getDescripcion());
      coleccionExistente.setFuentes(coleccion.getFuentes());
      coleccionExistente.setTitulo(coleccion.getTitulo());
      coleccionExistente.setHechos(coleccion.getHechos());

      return coleccionExistente;
    } else {
      this.colecciones.add(coleccion);
      return coleccion;
    }
  }
}
