package ar.edu.utn.frba.dds.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.IDAOSolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOColeccion;
import ar.edu.utn.frba.dds.agregador.models.repositories.DAO.impl.DAOSolicitudEliminacion;
import ar.edu.utn.frba.dds.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.domain.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudEliminacionRepository implements ISolicitudEliminacionRepository {
  IDAOSolicitudEliminacion dao;

  public SolicitudEliminacionRepository() {
    this.dao = new DAOSolicitudEliminacion();
  }

  public SolicitudEliminacion guardar(SolicitudEliminacion solicitud) {
    SolicitudEliminacion solicitudGuardada = this.dao.save(solicitud);
    return solicitudGuardada;
  }
}
