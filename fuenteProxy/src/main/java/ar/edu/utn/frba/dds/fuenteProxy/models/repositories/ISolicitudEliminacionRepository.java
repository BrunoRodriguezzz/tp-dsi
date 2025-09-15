package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
}
