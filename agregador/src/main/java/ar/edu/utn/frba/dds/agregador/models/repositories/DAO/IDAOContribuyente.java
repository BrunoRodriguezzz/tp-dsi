package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;

import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;

public interface IDAOContribuyente {
  public Contribuyente findById(Long id);
}
