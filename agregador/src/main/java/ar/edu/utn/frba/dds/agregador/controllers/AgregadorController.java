package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.ISeederService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
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
@CrossOrigin(origins = "http://localhost:8080")
public class AgregadorController {
  @Autowired
  private IAgregadorService agregadorService;

  @Autowired
  private ISeederService seederService;

  @GetMapping("/inicializacion")
  public ResponseEntity inicializarDatos() {
    this.seederService.init();
    return new ResponseEntity(HttpStatus.ACCEPTED);
  }

  @GetMapping("/hechos")
  public List<HechoOutputDTO> buscarHechos() {
    List<HechoOutputDTO> hechos = this.agregadorService.buscarHechos();
    return hechos;
  }

  @PostMapping("/hechos")
  public List<String> incorporarHecho(@RequestBody HechoInputDTO hecho) {
    List<String> incorporadoEn = this.agregadorService.incorporarHecho(hecho);
    return incorporadoEn;
  }

  // TODO: Borrar esto
  @GetMapping("/refresco")
  public void refrescarColecciones() {
    this.agregadorService.refrescarColecciones();
  }
}