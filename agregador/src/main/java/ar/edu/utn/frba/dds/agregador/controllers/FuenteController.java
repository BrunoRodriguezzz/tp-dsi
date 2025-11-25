package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.IAgregadorService;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.IFuenteService;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/fuentes")
public class FuenteController {
  private final IAgregadorService agregadorService;
  private final IFuenteService fuenteService;
  private final IColeccionService coleccionService;

  public FuenteController(IFuenteService fuenteService, IAgregadorService agregadorService, IColeccionService coleccionService) {
    this.fuenteService = fuenteService;
    this.agregadorService = agregadorService;
    this.coleccionService = coleccionService;
  }

  @PostMapping
  public ResponseEntity<FuenteOutputDTO> incorporarFuente(@RequestBody FuenteInputDTO fuente) {
    ValidadorInput.validarFuenteInputDTO(fuente);
    Fuente fuenteGuardada = this.agregadorService.incorporarFuente(fuente);
    FuenteOutputDTO fuenteOutputDTO = FuenteOutputDTO.toOutputDTO(fuenteGuardada);
    return ResponseEntity.status(HttpStatus.OK).body(fuenteOutputDTO);
  }

  @GetMapping
  public ResponseEntity<List<FuenteOutputDTO>> buscarFuentes() {
    List<FuenteOutputDTO> fuentes = fuenteService.buscarFuentesOutput();
    if (fuentes.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(fuentes);
  }

  @PostMapping("/{nombreFuente}/actualizar-colecciones")
  public ResponseEntity<String> actualizarColeccionesDeFuente(@PathVariable String nombreFuente) {
    try {
      coleccionService.actualizarColeccionesDeFuente(nombreFuente);
      return ResponseEntity.status(HttpStatus.OK).body("Colecciones actualizadas exitosamente");
    } catch (Exception e) {
      log.error("Error actualizando colecciones para la fuente {}: {}", nombreFuente, e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Error actualizando colecciones: " + e.getMessage());
    }
  }
}
