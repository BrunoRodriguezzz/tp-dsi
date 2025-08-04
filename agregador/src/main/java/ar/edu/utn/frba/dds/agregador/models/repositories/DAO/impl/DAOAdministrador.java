package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOAdministrador;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import java.util.ArrayList;
import java.util.List;

public class DAOAdministrador implements IDAOAdministrador {
  private List<Administrador> administradores;

  public DAOAdministrador() {
    administradores = new ArrayList<>();
    // TODO: Después borrar esto es solo prueba
    Administrador administrador = null;
    try {
      administrador = new Administrador("Ricardo", "Fordt");
    } catch (Exception e){System.out.println(e);}
    administrador.setId(1111L);
    administradores.add(administrador);
  }

  // TODO: Implementar el save

  public Administrador findById(Long id) {
    Administrador administrador = this.administradores.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    return administrador;
  }
}
