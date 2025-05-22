package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {
  @Autowired
  private IColeccionRepository coleccionRepository;

  public List<Coleccion> buscarColecciones() {
    return this.coleccionRepository.buscarColecciones();
  }
}
