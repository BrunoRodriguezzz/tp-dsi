package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOContribuyente;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOContribuyente;
import ar.edu.utn.frba.dds.agregador.models.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import org.springframework.stereotype.Repository;

@Repository
public class ContribuyenteRepository implements IContribuyenteRepository {
  private IDAOContribuyente dao;

  public ContribuyenteRepository() {
    this.dao = new DAOContribuyente();
  }

  @Override
  public Contribuyente buscarContribuyente(Long id) {
    return this.dao.findById(id);
  }
}
