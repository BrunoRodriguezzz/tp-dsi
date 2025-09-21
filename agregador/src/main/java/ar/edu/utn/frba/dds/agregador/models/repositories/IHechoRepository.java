package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findByOrigen(Origen origen);

    Optional<Hecho> findByFuentesAndIdsInternosFuentes(List<Fuente> fuentes, Map<Fuente, Long> idsInternosFuentes);

    List<Hecho> findByFuentes(List<Fuente> fuentes);
//  public Boolean inicializarHechos(List<Hecho> hechos);
//  public List<Hecho> buscarHechosGuardadosFuente(Fuente fuente);
}
