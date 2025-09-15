package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findByFuente(Fuente fuente);

    Optional<Hecho> findByFuente_IdAndIdInternoFuente(Long id, Long idInternoFuente);

    List<Hecho> findByOrigen(Origen origen);
//  public Boolean inicializarHechos(List<Hecho> hechos);
//  public List<Hecho> buscarHechosGuardadosFuente(Fuente fuente);
}
