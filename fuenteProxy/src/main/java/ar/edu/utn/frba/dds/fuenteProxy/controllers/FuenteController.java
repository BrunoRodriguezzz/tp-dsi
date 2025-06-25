package ar.edu.utn.frba.dds.fuenteProxy.controllers;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IFuenteService;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputFuenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FuenteController {
  @Autowired
  private IFuenteService fuenteService;

  @PostMapping
  private ResponseEntity guardarColeccion(@RequestBody InputFuenteDTO coleccion) {
    //TODO: Terminar
    return null;
  }
  // Acá vamos a tener los endpoints para la ABM de fuentes.
}
