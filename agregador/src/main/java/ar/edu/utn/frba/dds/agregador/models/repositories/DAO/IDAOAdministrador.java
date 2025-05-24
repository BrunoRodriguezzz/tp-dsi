package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;

import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Administrador;

public interface IDAOAdministrador {
  public Administrador findById(Long id);
}
