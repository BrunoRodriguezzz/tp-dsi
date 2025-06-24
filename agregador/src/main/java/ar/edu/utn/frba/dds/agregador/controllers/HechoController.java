package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
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
@RequestMapping("/hechos")
@CrossOrigin(origins = "http://localhost:8082")
public class HechoController {
  @Autowired
  private IHechoService hechoService;

  @Autowired
  private ISeederService seederService;

  @GetMapping
  public ResponseEntity buscarHechos() {
    List<HechoOutputDTO> hechos = this.hechoService.buscarHechos();
    if(hechos.isEmpty()){
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(hechos);
  }

  // Incorpora nuevos hechos que le envíen las fuentes(push based)
  @PostMapping
  public ResponseEntity incorporarHecho(@RequestBody HechoInputDTO hecho) {
    ValidadorInput.validarHechoInputDTO(hecho);
    List<String> incorporadoEn = this.hechoService.incorporarHecho(hecho);
    return ResponseEntity.status(HttpStatus.OK).body(incorporadoEn);
  }
}