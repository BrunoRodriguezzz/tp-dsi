package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findByFuente(Fuente fuente);
//  public Boolean inicializarHechos(List<Hecho> hechos);
//  public List<Hecho> buscarHechosGuardadosFuente(Fuente fuente);
}
