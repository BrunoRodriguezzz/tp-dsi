package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHechoRepository extends JpaRepository<Hecho, Long> {
}
