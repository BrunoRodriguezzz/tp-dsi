package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;

public interface IDAOAdministrador {
  public Administrador findById(Long id);
}
