package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.services.IColeccionService;
import ar.edu.utn.frba.dds.agregador.services.ISolicitudEliminacionService;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
  public SolicitudEliminacion guardar(@RequestBody SolicitudEliminacionInputDTO solicitud) {
    SolicitudEliminacion solicitudEliminacion =
        this.solicitudEliminacionService.guardar(
            solicitud
        );
    return solicitudEliminacion;
  }
}