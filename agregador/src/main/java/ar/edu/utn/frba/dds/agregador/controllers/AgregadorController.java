package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.ISeederService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agregador")
@CrossOrigin(origins = "http://localhost:8082")
public class AgregadorController {
  @Autowired
  private IAgregadorService agregadorService;

  @Autowired
  private ISeederService seederService;

  @GetMapping("/inicializacion")
  public ResponseEntity inicializarDatos() {
    this.seederService.init();
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/hechos")
  public ResponseEntity buscarHechos() {
    List<HechoOutputDTO> hechos = this.agregadorService.buscarHechos();
    if(hechos.isEmpty()){
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(hechos);
  }

  // Incorpora nuevos hechos que le envíen las fuentes(push based)
  @PostMapping("/hechos")
  public ResponseEntity incorporarHecho(@RequestBody HechoInputDTO hecho) {
    ValidadorInput.validarHechoInputDTO(hecho);
    List<String> incorporadoEn = this.agregadorService.incorporarHecho(hecho);
    return ResponseEntity.status(HttpStatus.OK).body(incorporadoEn);
  }

  // TODO: Borrar esto, creado para tests nada más
  @GetMapping("/refresco")
  public ResponseEntity refrescarColecciones() {
    this.agregadorService.refrescarColecciones();
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}