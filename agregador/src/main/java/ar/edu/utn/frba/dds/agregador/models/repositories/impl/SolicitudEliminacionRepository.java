package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOSolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOColeccion;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOSolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudEliminacionRepository implements ISolicitudEliminacionRepository {
  private final AtomicLong generadorIds = new AtomicLong(1);

  IDAOSolicitudEliminacion dao;

  public SolicitudEliminacionRepository() {
    this.dao = new DAOSolicitudEliminacion();
  }

  public SolicitudEliminacion guardarSolicitud(SolicitudEliminacion solicitud) {
    if(solicitud.getId() == null) {
      solicitud.setId(generadorIds.getAndIncrement());
    }
    SolicitudEliminacion solicitudGuardada = this.dao.save(solicitud);
    return solicitudGuardada;
  }

  public SolicitudEliminacion buscarSolicitud(Long id) {
    SolicitudEliminacion solicitud = this.dao.findById(id);
    return solicitud;
  }
}
