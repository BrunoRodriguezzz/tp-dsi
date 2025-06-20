package ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOSolicitudEliminacion;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Setter;

public class DAOSolicitudEliminacion implements IDAOSolicitudEliminacion {
  List<SolicitudEliminacion> solicitudesEliminacion;

  public DAOSolicitudEliminacion() {
    this.solicitudesEliminacion = new ArrayList<>();
  }

  public SolicitudEliminacion save(SolicitudEliminacion solicitud) {
    Optional<SolicitudEliminacion> existente = this.solicitudesEliminacion.stream()
        .filter(s -> s.getId().equals(solicitud.getId()))
        .findFirst();

    if (existente.isPresent()) {
      SolicitudEliminacion solicitudExistente = existente.get();

      solicitudExistente.setId(solicitud.getId());
      solicitudExistente.setHecho(solicitud.getHecho());
      solicitudExistente.setFundamento(solicitud.getFundamento());
      solicitudExistente.setEstadoSolicitudEliminacion(solicitud.getEstadoSolicitudEliminacion());
      solicitudExistente.setFechaCreacion(solicitud.getFechaCreacion());
      solicitudExistente.setResolucionSolicitudEliminacion(solicitud.getResolucionSolicitudEliminacion());
      solicitudExistente.setContribuyente(solicitud.getContribuyente());

      return solicitudExistente;
    } else {
      this.solicitudesEliminacion.add(solicitud);
      return solicitud;
    }
  }

  public SolicitudEliminacion findById(Long id) {
    SolicitudEliminacion solicitudEliminacion = this.solicitudesEliminacion.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    return solicitudEliminacion;
  }
}
