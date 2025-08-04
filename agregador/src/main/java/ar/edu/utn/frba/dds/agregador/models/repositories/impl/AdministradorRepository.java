package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOAdministrador;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOAdministrador;
import ar.edu.utn.frba.dds.agregador.models.repositories.IAdministradorRepository;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import org.springframework.stereotype.Repository;

@Repository
public class AdministradorRepository implements IAdministradorRepository {
  private IDAOAdministrador dao;

  public AdministradorRepository() {
    this.dao = new DAOAdministrador();
  }

  @Override
  public Administrador buscarAdministrador(Long id) {
    return this.dao.findById(id);
  }
}
