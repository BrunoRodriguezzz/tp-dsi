package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.EstadisticaHechos;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.HoraDelDia;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Provincia;
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

    /**
     * De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
     */
    @Query("SELECT e.provincia " +
            "FROM EstadisticaHechos e " +
            "WHERE e.coleccion.id = :idColeccion " +
            "GROUP BY e.provincia " +
            "ORDER BY SUM(e.cantidad_hechos) DESC " +
            "LIMIT 1")
    Provincia findProvinciaConMasHechosPorColeccion(@Param("idColeccion") Long idColeccion);

    /**
     * ¿Cuál es la categoría con mayor cantidad de hechos reportados?
     */
    @Query("SELECT e.categoria " +
            "FROM EstadisticaHechos e " +
            "GROUP BY e.categoria " +
            "ORDER BY SUM(e.cantidad_hechos) DESC " +
            "LIMIT 1")
    Categoria findCategoriaConMasHechos();

    /**
     * ¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
     */
    @Query("SELECT e.provincia " +
            "FROM EstadisticaHechos e " +
            "WHERE e.categoria.id = :idCategoria " +
            "GROUP BY e.provincia " +
            "ORDER BY SUM(e.cantidad_hechos) DESC " +
            "LIMIT 1")
    Provincia findProvinciaConMasHechosPorCategoria(@Param("idCategoria") Long idCategoria);

    /**
     * ¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
     */
    @Query("SELECT e.hora " +
            "FROM EstadisticaHechos e " +
            "WHERE e.categoria.id = :idCategoria " +
            "GROUP BY e.hora " +
            "ORDER BY SUM(e.cantidad_hechos) DESC " +
            "LIMIT 1")
    HoraDelDia findHoraConMasHechosPorCategoria(@Param("idCategoria") Long idCategoria);
}