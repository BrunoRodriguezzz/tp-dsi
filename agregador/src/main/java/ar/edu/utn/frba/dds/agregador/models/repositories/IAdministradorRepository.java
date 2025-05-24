package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.domain.models.entities.usuarios.Contribuyente;

public interface IContribuyenteRepository {
  public Contribuyente buscarContribuyente(Long id);
}
