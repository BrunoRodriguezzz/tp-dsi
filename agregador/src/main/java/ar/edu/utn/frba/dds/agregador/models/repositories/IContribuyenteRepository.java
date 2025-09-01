package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContribuyenteRepository extends JpaRepository<Contribuyente, Long> {

}
