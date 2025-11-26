package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IContribuyenteRepository extends JpaRepository<Contribuyente,Long> {

    Optional<Contribuyente> findByIdUsuario(Long idUsuario);
}
