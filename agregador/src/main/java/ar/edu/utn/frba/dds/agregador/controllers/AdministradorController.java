package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.models.dtos.input.AdministradorInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.AdministradorOutputDTO;
import ar.edu.utn.frba.dds.agregador.services.impl.AdministradorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administrador")
@Slf4j
public class AdministradorController {
  private AdministradorService administradorService;

  public AdministradorController(AdministradorService administradorService) {
    this.administradorService = administradorService;
  }

  @PostMapping()
  public ResponseEntity guardarAdministrador(@RequestBody AdministradorInputDTO administrador) {
    if(administrador.getNombre() == null || administrador.getNombre().isEmpty() || administrador.getApellido() == null || administrador.getApellido().isEmpty()) {
      throw new RuntimeException("El nombre y apellido del adminsitrador son obligatorios");
    }
    AdministradorOutputDTO administradorOutputDTO = this.administradorService.guardarAdministrador(administrador);
    return ResponseEntity.status(HttpStatus.CREATED).body(administradorOutputDTO);
  }
}
