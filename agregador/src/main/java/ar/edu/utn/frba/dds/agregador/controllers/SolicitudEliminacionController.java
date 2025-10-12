package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.controllers.validadores.ValidadorInput;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.AdministradorInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.GestionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.ISolicitudEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/solicitudesEliminacion")
@CrossOrigin(origins = "http://localhost:8080")
public class SolicitudEliminacionController {
  @Autowired
  private ISolicitudEliminacionService solicitudEliminacionService;

  @PostMapping
  public ResponseEntity<SolicitudEliminacionOutputDTO> guardarSolicitud(@RequestBody SolicitudEliminacionInputDTO solicitud) {
    ValidadorInput.validarSolicitudInputDTO(solicitud);
    SolicitudEliminacionOutputDTO solicitudEliminacion =
        this.solicitudEliminacionService.guardarSolicitud(
            solicitud
        );
    return ResponseEntity.status(HttpStatus.CREATED).body(solicitudEliminacion);
  }

  @GetMapping()
  public ResponseEntity<List<SolicitudEliminacionOutputDTO>> buscarTodasLasSolicitudes() {
    List<SolicitudEliminacionOutputDTO> solicitudes = this.solicitudEliminacionService.buscarTodasLasSolicitudes();
    return ResponseEntity.status(HttpStatus.OK).body(solicitudes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SolicitudEliminacionOutputDTO> buscarSolicitud(@PathVariable("id") Long id) {
    SolicitudEliminacionOutputDTO solicitudEliminacion = this.solicitudEliminacionService.buscarSolicitud(id);
    return ResponseEntity.status(HttpStatus.OK).body(solicitudEliminacion);
  }

  @PatchMapping("rechazo/{id}")
  // TODO: Buscar los admins
  public ResponseEntity<SolicitudEliminacionOutputDTO> rechazarSolicitud(@RequestBody GestionInputDTO input, @PathVariable("id") Long id) {
    ValidadorInput.validarGestionInputDTO(input);
    SolicitudEliminacionOutputDTO rechazada = this.solicitudEliminacionService.rechazarSolicitud(input.getIdAdministrador(), id);
    return ResponseEntity.status(HttpStatus.OK).body(rechazada);
  }

  @PatchMapping("aceptacion/{id}")
  // TODO: Buscar los admins
  public ResponseEntity<SolicitudEliminacionOutputDTO> aceptarSolicitud(@RequestBody GestionInputDTO input, @PathVariable("id") Long id) {
    ValidadorInput.validarGestionInputDTO(input);
    SolicitudEliminacionOutputDTO aceptada = this.solicitudEliminacionService.aceptarSolicitud(input.getIdAdministrador(), id);
    return ResponseEntity.status(HttpStatus.OK).body(aceptada);
  }
}