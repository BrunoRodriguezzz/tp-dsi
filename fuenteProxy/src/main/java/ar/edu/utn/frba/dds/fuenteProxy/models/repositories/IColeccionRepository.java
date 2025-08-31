package ar.edu.utn.frba.dds.fuenteProxy.models.repositories;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Coleccion;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
//    List<Coleccion> getAll();
//    Coleccion getById(Long id);
//    void guardarColeccion(Coleccion coleccion);
}
