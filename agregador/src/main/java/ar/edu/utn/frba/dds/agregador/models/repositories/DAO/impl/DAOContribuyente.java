package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOContribuyente;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAOContribuyente implements IDAOContribuyente {
  private List<Contribuyente> contribuyentes;

  public DAOContribuyente() {
    contribuyentes = new ArrayList<>();
    // TODO: Después borrar esto es solo prueba
    Contribuyente contribuyente = null;
    try {
      contribuyente = new Contribuyente("Valentin", "Bravo", LocalDate.of(2004, 8, 5));
    } catch (Exception e){System.out.println(e);}
    contribuyente.setId(20040508L);
    contribuyentes.add(contribuyente);
  }

  // TODO: Implementar el save

  public Contribuyente findById(Long id) {
    Contribuyente contribuyente = this.contribuyentes.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    return contribuyente;
  }
}
