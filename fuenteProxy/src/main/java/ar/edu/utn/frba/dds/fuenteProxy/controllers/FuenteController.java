package ar.edu.utn.frba.dds.fuenteProxy.controllers;

import ar.edu.utn.frba.dds.fuenteProxy.Services.IFuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FuenteController {
  @Autowired
  private IFuenteService fuenteService;

  // Acá vamos a tener los endpoints para la ABM de fuentes.
}
