package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/colecciones")
@CrossOrigin(origins = "http://localhost:8080")
public class ColeccionController {
  @Autowired
  private IColeccionService coleccionService;

  @GetMapping
  public List<Coleccion> buscarColecciones() {
    List<Coleccion> colecciones = this.coleccionService.buscarColecciones();
    return colecciones;
  }
}
