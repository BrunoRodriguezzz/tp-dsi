package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Administrador;
import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;

public interface IAdministradorRepository {
  public Administrador buscarAdministrador(Long id);
}
