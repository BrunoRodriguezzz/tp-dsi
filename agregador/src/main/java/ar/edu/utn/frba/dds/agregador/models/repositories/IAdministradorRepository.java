package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;

public interface IAdministradorRepository {
  public Administrador buscarAdministrador(Long id);
}
