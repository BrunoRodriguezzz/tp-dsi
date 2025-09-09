package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.utils.EstadisticaCombinacion;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.HoraDelDia;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEstadisticaCombinacionRepository extends JpaRepository<EstadisticaCombinacion, Long> {

    @Query("SELECT e FROM EstadisticaCombinacion e WHERE " +
            "e.categoria = :#{#estadistica.categoria} AND " +
            "e.coleccion = :#{#estadistica.coleccion} AND " +
            "e.provincia = :#{#estadistica.provincia} AND " +
            "e.hora = :#{#estadistica.hora}")
    Optional<EstadisticaCombinacion> findByAllFieldsExceptId(@Param("estadistica") EstadisticaCombinacion estadistica);

    /**
     * De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
     */
    @Query("SELECT e.provincia " +
            "FROM EstadisticaCombinacion e " +
            "JOIN e.hechos h " +
            "WHERE e.coleccion.id = :idColeccion " +
            "GROUP BY e.provincia " +
            "ORDER BY COUNT(DISTINCT h.id) DESC " +
            "LIMIT 1")
    Provincia findProvinciaConMasHechosPorColeccion(@Param("idColeccion") Long idColeccion);

    /**
     * ¿Cuál es la categoría con mayor cantidad de hechos reportados?
     */
    @Query("SELECT e.categoria " +
            "FROM EstadisticaCombinacion e " +
            "JOIN e.hechos h " +
            "GROUP BY e.categoria " +
            "ORDER BY COUNT(DISTINCT h.id) DESC " +
            "LIMIT 1")
    Categoria findCategoriaConMasHechos();

    /**
     * ¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
     */
    @Query("SELECT e.provincia " +
            "FROM EstadisticaCombinacion e " +
            "JOIN e.hechos h " +
            "WHERE e.categoria.id = :idCategoria " +
            "GROUP BY e.provincia " +
            "ORDER BY COUNT(DISTINCT h.id) DESC " +
            "LIMIT 1")
    Provincia findProvinciaConMasHechosPorCategoria(@Param("idCategoria") Long idCategoria);

    /**
     * ¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
     */
    @Query("SELECT e.hora " +
            "FROM EstadisticaCombinacion e " +
            "JOIN e.hechos h " +
            "WHERE e.categoria.id = :idCategoria " +
            "GROUP BY e.hora " +
            "ORDER BY COUNT(DISTINCT h.id) DESC " +
            "LIMIT 1")
    HoraDelDia findHoraConMasHechosPorCategoria(@Param("idCategoria") Long idCategoria);
}