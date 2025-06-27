package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.domain.criterio.Criterio;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.CriterioInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import java.time.LocalDate;
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
      @RequestParam(required = false) LocalDate fechaInicio,
      @RequestParam(required = false) LocalDate fechaFin,
      @RequestParam(required = false) String titulo
  ) {
    QueryParamsFiltro params = new QueryParamsFiltro();
    params.setCategoria(categoria);
    params.setFechaAcontecimientoInicio(fechaInicio);
    params.setFechaAcontecimientoFin(fechaFin);
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

  // TODO: Fijarse que ande bien
  @PutMapping("/{id}/adicion/criterio")
  public ResponseEntity agregarFiltrosACriterioColeccion(@PathVariable("id") Long id, @RequestBody CriterioInputDTO criterio) {
    ValidadorInput.validarCriterioInput(criterio);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.agregarFiltrosCriterio(id, criterio);
    return ResponseEntity.status(HttpStatus.CREATED).body(coleccionOutputDTO);
  }

  // TODO: Fijarse que ande bien
  @PutMapping("/{id}/adicion/fuente")
  public ResponseEntity agregarFuenteAColeccion(@PathVariable("id") Long id, @RequestBody FuenteInputDTO fuenteInputDTO) {
    ValidadorInput.validarFuenteInputDTO(fuenteInputDTO);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.agregarFuenteAColeccion(id, fuenteInputDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(coleccionOutputDTO);
  }

  // TODO: Fijarse que ande bien
  @PutMapping("/{id}/eliminacion/fuente")
  public ResponseEntity quitarFuentesAColeccion(@PathVariable("id") Long id, @RequestBody List<FuenteInputDTO> fuentesInputDTO) {
    fuentesInputDTO.stream().forEach(ValidadorInput::validarFuenteInputDTO);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.quitarFuentesAColeccion(id, fuentesInputDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(coleccionOutputDTO);
  }

  // TODO: Sacar filtros al criterio
  // TODO: Fijarse que ande bien
  @PutMapping("/{id}/eliminacion/criterio")
  public ResponseEntity quitarFiltrosACriterioColeccion(@PathVariable("id") Long id, @RequestBody CriterioInputDTO criterio) {
    ValidadorInput.validarCriterioInput(criterio);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.quitarFiltrosCriterio(id, criterio);
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

  // TODO: Borrar esto, creado para tests nada más
  @GetMapping("/refresco")
  public ResponseEntity refrescarColecciones() {
    this.coleccionService.refrescarColecciones();
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
