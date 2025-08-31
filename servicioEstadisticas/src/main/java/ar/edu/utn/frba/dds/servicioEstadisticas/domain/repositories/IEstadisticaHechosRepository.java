package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.EstadisticaHechos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEstadisticaHechosRepository extends JpaRepository<EstadisticaHechos, Long> {
    @Query("SELECT e FROM EstadisticaHechos e WHERE " +
            "e.categoria = :#{#estadistica.categoria} AND " +
            "e.coleccion = :#{#estadistica.coleccion} AND " +
            "e.provincia = :#{#estadistica.provincia} AND " +
            "e.hora = :#{#estadistica.hora}")
    Optional<EstadisticaHechos> findByAllFieldsExceptId(@Param("estadistica") EstadisticaHechos estadistica);
}
