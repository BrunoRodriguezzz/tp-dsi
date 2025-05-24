package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity buscarColecciones() {
    List<ColeccionOutputDTO> colecciones = this.coleccionService.buscarColecciones();
    if(colecciones.isEmpty()) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.FOUND).body(colecciones);
  }

  @GetMapping("/{id}")
  public ResponseEntity buscarColeccion(@PathVariable("id") Long id) {
    ColeccionOutputDTO coleccion = this.coleccionService.buscarColeccion(id);
    if(coleccion == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.FOUND).body(coleccion);
  }
}
