package ar.edu.utn.frba.dds.agregador.models.repositories;


import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.TipoFuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFuenteRepository extends JpaRepository<Fuente, Long> {
    Fuente findByNombre(String nombre);
    Fuente findByIdInternoFuenteAndTipoFuente(Long idInternoFuente, TipoFuente tipoFuente);
//  public Fuente buscarFuente(String nombre);
}
