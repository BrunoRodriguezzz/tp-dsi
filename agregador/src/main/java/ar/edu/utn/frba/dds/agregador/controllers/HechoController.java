package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hechos")
@CrossOrigin(origins = "http://localhost:8082")
public class HechoController {
  @Autowired
  private IHechoService hechoService;

  @Autowired
  private IAgregadorService agregadorService;

  @GetMapping()
  public ResponseEntity<Page<HechoOutputDTO>> buscarHechos(
      @RequestParam(name = "categoria", required = false) String categoria,
      @RequestParam(name = "fechaAcontecimientoInicio", required = false) LocalDateTime fechaAcontecimientoInicio,
      @RequestParam(name = "fechaAcontecimientoFin", required = false) LocalDateTime fechaAcontecimientoFin,
      @RequestParam(name = "titulo", required = false) String titulo,
      @RequestParam(name = "latitud", required = false) Double latitud,
      @RequestParam(name = "longitud", required = false) Double longitud,
      @RequestParam(name = "fechaCargaInicio", required = false) LocalDateTime fechaCargaInicio,
      @RequestParam(name = "fechaCargaFin", required = false) LocalDateTime fechaCargaFin,
      @RequestParam(name = "fuente", required = false) String fuente,
      Pageable pageable
  ) {
      QueryParamsFiltro params = Utils.crearFiltros(categoria, fechaAcontecimientoInicio,
              fechaAcontecimientoFin, titulo, latitud, longitud, fechaCargaInicio, fechaCargaFin, fuente);
      Page<HechoOutputDTO> hechos = this.hechoService.buscarHechos(params, pageable);
    return ResponseEntity.ok(hechos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<HechoOutputDTO> buscarHechoPorID(@PathVariable(name = "id") Long id) {
    HechoOutputDTO hecho = HechoOutputDTO.HechoToDTO(this.hechoService.buscarHecho(id));
    return ResponseEntity.ok(hecho);
  }

  @PutMapping("/{id}")
  public ResponseEntity<HechoOutputDTO> actualizarHecho(@RequestBody HechoInputDTO hecho, @PathVariable(name = "id") Long id) {
      HechoOutputDTO hechoActualizado = this.hechoService.actualizarHecho(hecho, id);
      return ResponseEntity.ok(hechoActualizado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarHecho(@PathVariable(name = "id") Long id) {
    this.agregadorService.eliminarHecho(id);
    return ResponseEntity.noContent().build();
  }

  // Incorpora nuevos hechos que le envíen las fuentes(push based)
  @PostMapping
  public ResponseEntity<List<String>> incorporarHecho(@RequestBody HechoInputDTO hecho) {
    ValidadorInput.validarHechoInputDTO(hecho);
    List<String> incorporadoEn = this.agregadorService.incorporarHecho(hecho);
    return ResponseEntity.ok(incorporadoEn);
  }
}