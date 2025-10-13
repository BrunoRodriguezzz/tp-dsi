package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHechoRepository extends JpaRepository<Hecho, Long> {
}
