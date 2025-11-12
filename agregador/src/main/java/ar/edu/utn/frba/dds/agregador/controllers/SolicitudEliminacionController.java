package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.GestionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.ISolicitudEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudesEliminacion")
@CrossOrigin(origins = "http://localhost:8080")
public class SolicitudEliminacionController {
  @Autowired
  private ISolicitudEliminacionService solicitudEliminacionService;

  @PostMapping
  public ResponseEntity guardarSolicitud(@RequestBody SolicitudEliminacionInputDTO solicitud) {
    ValidadorInput.validarSolicitudInputDTO(solicitud);
    SolicitudEliminacionOutputDTO solicitudEliminacion = this.solicitudEliminacionService.guardarSolicitud(solicitud);
    return ResponseEntity.status(HttpStatus.CREATED).body(solicitudEliminacion);
  }

  @GetMapping("/{id}")
  public ResponseEntity buscarSolicitud(@PathVariable("id") Long id) {
    SolicitudEliminacionOutputDTO solicitudEliminacion = this.solicitudEliminacionService.buscarSolicitud(id);
    if(solicitudEliminacion == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.OK).body(solicitudEliminacion);
  }

  @GetMapping()
  public ResponseEntity<List<SolicitudEliminacionOutputDTO>> buscarSolicitudes() {
    List<SolicitudEliminacionOutputDTO> solicitudes = this.solicitudEliminacionService.buscarSolicitudes();
    return ResponseEntity.ok(solicitudes);
  }

  @PatchMapping("rechazo/{id}")
  // TODO: Buscar los admins
  public ResponseEntity rechazarSolicitud(@RequestBody GestionInputDTO input, @PathVariable("id") Long id) {
    ValidadorInput.validarGestionInputDTO(input);
    SolicitudEliminacionOutputDTO rechazada = this.solicitudEliminacionService.rechazarSolicitud(input, id);
    return ResponseEntity.status(HttpStatus.OK).body(rechazada);
  }

  @PatchMapping("aceptacion/{id}")
  // TODO: Buscar los admins
  public ResponseEntity aceptarSolicitud(@RequestBody GestionInputDTO input, @PathVariable("id") Long id) {
    ValidadorInput.validarGestionInputDTO(input);
    SolicitudEliminacionOutputDTO aceptada = this.solicitudEliminacionService.aceptarSolicitud(input, id);
    return ResponseEntity.status(HttpStatus.OK).body(aceptada);
  }
}