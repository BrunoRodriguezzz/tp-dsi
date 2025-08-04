package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fuentes")
public class FuenteController {
  @Autowired
  private IFuenteService fuenteService;

  @PostMapping
  public ResponseEntity<Fuente> incorporarFuente(@RequestBody FuenteInputDTO fuente) {
    ValidadorInput.validarFuenteInputDTO(fuente);
    Fuente fuenteIncorporada = this.fuenteService.incorporarFuente(fuente);
    return ResponseEntity.status(HttpStatus.OK).body(fuenteIncorporada);
  }

  @GetMapping
  public ResponseEntity<List<FuenteOutputDTO>> buscarFuentes() {
    List<FuenteOutputDTO> fuentes = fuenteService.buscarFuentesOutput();
    if (fuentes.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(fuentes);
  }
}
