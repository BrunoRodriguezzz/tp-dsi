package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.List;

public interface IColeccionService {
  public List<Coleccion> buscarColecciones();
}
