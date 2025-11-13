
package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.AdministradorInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.output.AdministradorOutputDTO;
import ar.edu.utn.frba.dds.agregador.models.repositories.IAdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {
  private final IAdministradorRepository administradorRepository;

  public AdministradorService(IAdministradorRepository administradorRepository) {
    this.administradorRepository = administradorRepository;
  }

  public AdministradorOutputDTO guardarAdministrador(AdministradorInputDTO administradorInput) {
    Administrador administrador = AdministradorInputDTO.DTOtoAdministrador(administradorInput);
    administrador = this.administradorRepository.save(administrador);
    AdministradorOutputDTO administradorOutputDTO = AdministradorOutputDTO.AdministradorToDTO(administrador);
    return administradorOutputDTO;
  }

}
