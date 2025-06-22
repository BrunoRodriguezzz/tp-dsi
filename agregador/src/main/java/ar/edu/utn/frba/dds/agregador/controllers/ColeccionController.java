package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    return ResponseEntity.status(HttpStatus.OK).body(colecciones);
  }

  @GetMapping("/{id}/hechos")
  public ResponseEntity buscarHechosColeccion(
      @PathVariable("id") Long id,
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) String fechaInicio,
      @RequestParam(required = false) String fechaFin,
      @RequestParam(required = false) String titulo
  ) {
    QueryParamsFiltro params = new QueryParamsFiltro();
    params.setCategoria(categoria);
    params.setFechaInicio(fechaInicio);
    params.setFechaFin(fechaFin);
    params.setTitulo(titulo);

    List<HechoOutputDTO> hechos = this.coleccionService.buscarHechosColeccion(id,params);
    if(hechos == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.OK).body(hechos);
  }

  @GetMapping("/{id}")
  public ResponseEntity buscarColeccion(@PathVariable("id") Long id) {
    ColeccionOutputDTO coleccion = this.coleccionService.buscarColeccion(id);
    if(coleccion == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.OK).body(coleccion);
  }

  @PostMapping()
  public ResponseEntity guardarColeccion(@RequestBody ColeccionInputDTO coleccion) {
    ValidadorInput.validarColeccionInput(coleccion);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.guardarColeccion(coleccion);
    return ResponseEntity.status(HttpStatus.CREATED).body(coleccionOutputDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity actualizarColeccion(@PathVariable("id") Long id, @RequestBody ColeccionInputDTO coleccion) {
    if (id == null || id <= 0) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    ValidadorInput.validarColeccionInput(coleccion);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.actualizarColeccion(id, coleccion);
    return ResponseEntity.status(HttpStatus.OK).body(coleccionOutputDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity eliminarColeccion(@PathVariable("id") Long id) {
    if (id == null || id <= 0) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    coleccionService.eliminarColeccion(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
