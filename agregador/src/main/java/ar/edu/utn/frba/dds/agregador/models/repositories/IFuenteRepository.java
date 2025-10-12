
package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IFuenteRepository extends JpaRepository<Fuente, Long> {
    Fuente findByNombre(String nombre);
    List<Fuente> findByTipoFuente(TipoFuente tipoFuente);

    List<Fuente> findByFechaCargaBetween(LocalDateTime minus, LocalDateTime now);
}
