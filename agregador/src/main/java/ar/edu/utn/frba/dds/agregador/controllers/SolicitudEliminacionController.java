package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.AdministradorInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.ISolicitudEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitudesEliminacion")
@CrossOrigin(origins = "http://localhost:8080")
public class SolicitudEliminacionController {
  @Autowired
  private ISolicitudEliminacionService solicitudEliminacionService;

  @PostMapping
  public SolicitudEliminacionOutputDTO guardar(@RequestBody SolicitudEliminacionInputDTO solicitud) {
    SolicitudEliminacionOutputDTO solicitudEliminacion =
        this.solicitudEliminacionService.guardar(
            solicitud
        );
    return solicitudEliminacion;
  }

  @PatchMapping("rechazo/{id}")
  // TODO: Buscar los admins
  public SolicitudEliminacionOutputDTO rechazar(@RequestBody AdministradorInputDTO administrador, @PathVariable("id") Long id) {
    SolicitudEliminacionOutputDTO resuelta = this.solicitudEliminacionService.rechazar(administrador, id);
    return resuelta;
  }

  @GetMapping("/{id}")
  public SolicitudEliminacionOutputDTO buscarSolicitud(@PathVariable("id") Long id) {
    SolicitudEliminacionOutputDTO solicitudEliminacionOutputDTO = this.solicitudEliminacionService.buscarSolicitud(id);
    return solicitudEliminacionOutputDTO;
  }
}