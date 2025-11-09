package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.ResolucionSolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IResolucionSolicitudEliminacionRepository extends JpaRepository<ResolucionSolicitudEliminacion, Long> {
}
