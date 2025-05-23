package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

public class DAOSolicitudEliminacion implements IDAOSolicitudEliminacion {
  List<SolicitudEliminacion> solicitudesEliminacion;

  public DAOSolicitudEliminacion() {
    this.solicitudesEliminacion = new ArrayList<>();
  }

  public SolicitudEliminacion save(SolicitudEliminacion solicitud) {
    this.solicitudesEliminacion.add(solicitud);
    return solicitud;
  }

  public SolicitudEliminacion findById(Long id) {
    SolicitudEliminacion solicitudEliminacion = this.solicitudesEliminacion.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    return solicitudEliminacion;
  }
}
