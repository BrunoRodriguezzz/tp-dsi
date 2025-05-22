package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.Collections;
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

  public List<String> incorporarHecho(Hecho hecho) {
    List<Coleccion> colecciones = this.coleccionRepository.buscarColecciones();
    List<String> nombreColecciones = new ArrayList<>();
    colecciones.forEach(coleccion -> {
      List<Hecho> listaHecho = Collections.singletonList(hecho);
      boolean resultado = coleccion.cargarHecho(hecho);
      if (resultado) {
        nombreColecciones.add(coleccion.getTitulo());
      }
    });
    Boolean resultado = this.coleccionRepository.guardarColecciones(colecciones);
    if(!resultado){
      System.out.println("No se pudo guardar las colecciones");
    }
    return nombreColecciones;
  }
}
