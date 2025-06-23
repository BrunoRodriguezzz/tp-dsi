package ar.edu.utn.frba.dds.agregador.models.repositories.DAO;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;

public interface IDAOContribuyente {
  public Contribuyente findById(Long id);
}
