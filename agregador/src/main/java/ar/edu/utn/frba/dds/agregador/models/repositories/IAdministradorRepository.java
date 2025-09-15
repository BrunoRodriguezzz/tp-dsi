package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdministradorRepository extends JpaRepository<Administrador, Long> {

}
