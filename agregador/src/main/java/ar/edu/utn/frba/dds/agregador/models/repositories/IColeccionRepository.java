package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
  List<Coleccion> findColeccionsByHechos(List<Hecho> hechos);
//  public Boolean eliminarHechoDeColecciones(Hecho hecho); TODO
//  public List<Coleccion> buscarCopiaColecciones();
//  public Coleccion buscarCopiaColeccion(Long id);
}
