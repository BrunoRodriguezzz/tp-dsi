package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.domain.consenso.Consenso;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.*;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public ResponseEntity<Page<ColeccionOutputDTO>> buscarColecciones(Pageable pageable) {
    Page<ColeccionOutputDTO> colecciones = this.coleccionService.buscarColecciones(pageable);
    return ResponseEntity.ok(colecciones);
  }

  @GetMapping("/{id}/hechos")
  public ResponseEntity buscarHechosColeccion(
      @PathVariable("id") Long id,
      @RequestParam(name = "categoria", required = false) String categoria,
      @RequestParam(name = "fechaAcontecimientoInicio", required = false) LocalDateTime fechaAcontecimientoInicio,
      @RequestParam(name = "fechaAcontecimientoFin", required = false) LocalDateTime fechaAcontecimientoFin,
      @RequestParam(name = "titulo", required = false) String titulo,
      @RequestParam(name = "latitud", required = false) String latitud,
      @RequestParam(name = "longitud", required = false) String longitud,
      @RequestParam(name = "fechaCargaInicio", required = false) LocalDateTime fechaCargaInicio,
      @RequestParam(name = "fechaCargaFin", required = false) LocalDateTime fechaCargaFin
  ) {
    QueryParamsFiltro params = Utils.crearFiltros(categoria, fechaAcontecimientoInicio, fechaAcontecimientoFin,
            titulo, latitud, longitud, fechaCargaInicio, fechaCargaFin);

    List<HechoOutputDTO> hechos = this.coleccionService.buscarHechosColeccion(id,params);
    return ResponseEntity.status(HttpStatus.OK).body(hechos);
  }

  @GetMapping("/{id}/hechos/curados")
  public ResponseEntity buscarHechosCuradosColeccion(
      @PathVariable("id") Long id,
      @RequestParam(name = "categoria", required = false) String categoria,
      @RequestParam(name = "fechaAcontecimientoInicio", required = false) LocalDateTime fechaAcontecimientoInicio,
      @RequestParam(name = "fechaAcontecimientoFin", required = false) LocalDateTime fechaAcontecimientoFin,
      @RequestParam(name = "titulo", required = false) String titulo,
      @RequestParam(name = "latitud", required = false) String latitud,
      @RequestParam(name = "longitud", required = false) String longitud,
      @RequestParam(name = "fechaCargaInicio", required = false) LocalDateTime fechaCargaInicio,
      @RequestParam(name = "fechaCargaFin", required = false) LocalDateTime fechaCargaFin
  ) {
    QueryParamsFiltro params = Utils.crearFiltros(categoria, fechaAcontecimientoInicio, fechaAcontecimientoFin, titulo,
            latitud, longitud, fechaCargaInicio, fechaCargaFin);

    List<HechoOutputDTO> hechos = this.coleccionService.buscarHechosCuradosColeccion(id,params);
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
  @PutMapping("/{id}/eliminacion/criterio")
  public ResponseEntity quitarFiltrosACriterioColeccion(@PathVariable("id") Long id, @RequestBody CriterioInputDTO criterio) {
    ValidadorInput.validarCriterioInput(criterio);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.quitarFiltrosCriterio(id, criterio);
    return ResponseEntity.status(HttpStatus.CREATED).body(coleccionOutputDTO);
  }

  // TODO: Fijarse que ande bien
  @PutMapping("/{id}/adicion/fuente")
  public ResponseEntity agregarFuenteAColeccion(@PathVariable("id") Long id, @RequestBody NombreFuenteInputDTO fuenteInputDTO) {
    ValidadorInput.validarNombreFuente(fuenteInputDTO);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.agregarFuenteAColeccion(id, fuenteInputDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(coleccionOutputDTO);
  }

  // TODO: Fijarse que ande bien
  @PutMapping("/{id}/eliminacion/fuente")
  public ResponseEntity quitarFuentesAColeccion(@PathVariable("id") Long id, @RequestBody List<NombreFuenteInputDTO> fuentesInputDTO) {
    fuentesInputDTO.forEach(ValidadorInput::validarNombreFuente);
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.quitarFuentesAColeccion(id, fuentesInputDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(coleccionOutputDTO);
  }

  @PutMapping("/{id}/adicion")
  public ResponseEntity agregarConsensoAColeccion(@PathVariable("id") Long id, @RequestParam (name = "consenso", required = false) String consenso) {
    ColeccionOutputDTO coleccionOutputDTO = coleccionService.agregarConsensoAColeccion(id, Consenso.valueOf(consenso));
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
