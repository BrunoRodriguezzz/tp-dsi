package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.QueryParamsFiltro;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import ar.edu.utn.frba.dds.agregador.services.ISeederService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hechos")
@CrossOrigin(origins = "http://localhost:8082")
public class HechoController {
  @Autowired
  private IHechoService hechoService;

  @Autowired
  private IAgregadorService agregadorService;

  @Autowired
  private ISeederService seederService;

  @GetMapping()
  public ResponseEntity buscarHechos(
      @RequestParam(name = "categoria", required = false) String categoria,
      @RequestParam(name = "fechaAcontecimientoInicio", required = false) LocalDate fechaAcontecimientoInicio,
      @RequestParam(name = "fechaAcontecimientoFin", required = false) LocalDate fechaAcontecimientoFin,
      @RequestParam(name = "titulo", required = false) String titulo
  ) {
    QueryParamsFiltro params = new QueryParamsFiltro();
    params.setCategoria(categoria);
    params.setFechaAcontecimientoInicio(fechaAcontecimientoInicio);
    params.setFechaAcontecimientoFin(fechaAcontecimientoFin);
    params.setTitulo(titulo);

    List<HechoOutputDTO> hechos = this.hechoService.buscarHechos(params);
    if(hechos == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.OK).body(hechos);
  }

  // Incorpora nuevos hechos que le envíen las fuentes(push based)
  @PostMapping
  public ResponseEntity incorporarHecho(@RequestBody HechoInputDTO hecho) {
    ValidadorInput.validarHechoInputDTO(hecho);
    List<String> incorporadoEn = this.agregadorService.incorporarHecho(hecho);
    return ResponseEntity.status(HttpStatus.OK).body(incorporadoEn);
  }
}