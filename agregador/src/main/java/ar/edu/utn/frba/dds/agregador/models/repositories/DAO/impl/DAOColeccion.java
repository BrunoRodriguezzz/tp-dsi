package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOColeccion;
import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
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

  public Coleccion find(Long id) {
    Coleccion coleccion = this.colecciones.stream().filter(c ->
        c.getId().equals(id)).findFirst().orElse(null);
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

  public Boolean eliminarHechoDeColecciones(Long id) {
    this.colecciones.forEach(coleccion -> {
      coleccion.getHechos().removeIf(h -> h.getId().equals(id));
    });
    return true;
  }

  @Override
  public void eliminarColeccion(Long id) {
    Coleccion coleccion = this.colecciones.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    if (coleccion == null) {
      throw new RuntimeException("La coleccion a eliminar no existe");
    }
    this.colecciones.remove(coleccion);
  }
}
