package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;

public interface IContribuyenteRepository {
  public Contribuyente buscarContribuyente(Long id);
}
