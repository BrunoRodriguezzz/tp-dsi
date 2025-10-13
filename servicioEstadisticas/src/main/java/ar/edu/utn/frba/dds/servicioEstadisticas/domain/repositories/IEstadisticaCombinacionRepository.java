package ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.HoraDelDia;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.dimensiones.Provincia;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.utils.EstadisticaCombinacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEstadisticaCombinacionRepository extends JpaRepository<EstadisticaCombinacion, Long> {
    @Query("SELECT e " +
        "FROM EstadisticaCombinacion e " +
        "WHERE e.coleccion = :#{#estadistica.coleccion} " +
        "AND e.categoria = :#{#estadistica.categoria} " +
        "AND e.provincia = :#{#estadistica.provincia} " +
        "AND e.hora = :#{#estadistica.hora}")
    Optional<EstadisticaCombinacion> findByAllFieldsExceptId(@Param("estadistica") EstadisticaCombinacion estadistica);

    // mapa de provincias por colección
    @Query("SELECT e.provincia as provincia, COUNT(DISTINCT h.id) as cantidad " +
        "FROM EstadisticaCombinacion e " +
        "JOIN e.hechos h " +
        "WHERE e.coleccion.id = :idColeccion " +
        "GROUP BY e.provincia " +
        "ORDER BY COUNT(DISTINCT h.id) DESC")
    List<Object[]> findProvinciaConHechosPorColeccion(@Param("idColeccion") Long idColeccion);

    // mapa de categorías con más hechos (máximo 30)
    @Query("SELECT e.categoria as categoria, COUNT(DISTINCT h.id) as cantidad " +
        "FROM EstadisticaCombinacion e " +
        "JOIN e.hechos h " +
        "GROUP BY e.categoria " +
        "ORDER BY COUNT(DISTINCT h.id) DESC " +
        "LIMIT 30")
    List<Object[]> findCategoriasConHechos();

    // mapa de provincias por categoría
    @Query("SELECT e.provincia as provincia, COUNT(DISTINCT h.id) as cantidad " +
        "FROM EstadisticaCombinacion e " +
        "JOIN e.hechos h " +
        "WHERE e.categoria.id = :idCategoria " +
        "GROUP BY e.provincia " +
        "ORDER BY COUNT(DISTINCT h.id) DESC")
    List<Object[]> findProvinciasConHechosPorCategoria(@Param("idCategoria") Long idCategoria);

    // mapa de horas por categoría
    @Query("SELECT e.hora as hora, COUNT(DISTINCT h.id) as cantidad " +
        "FROM EstadisticaCombinacion e " +
        "JOIN e.hechos h " +
        "WHERE e.categoria.id = :idCategoria " +
        "GROUP BY e.hora " +
        "ORDER BY COUNT(DISTINCT h.id) DESC")
    List<Object[]> findHorasConHechosPorCategoria(@Param("idCategoria") Long idCategoria);

    // UNA provincia con más hechos por colección
    @Query("SELECT e.provincia " +
        "FROM EstadisticaCombinacion e " +
        "JOIN e.hechos h " +
        "WHERE e.coleccion.id = :idColeccion " +
        "GROUP BY e.provincia " +
        "ORDER BY COUNT(DISTINCT h.id) DESC " +
        "LIMIT 1")
    Provincia findProvinciaConMasHechosPorColeccion(@Param("idColeccion") Long idColeccion);

    // UNA categoría con más hechos
    @Query("SELECT e.categoria " +
        "FROM EstadisticaCombinacion e " +
        "JOIN e.hechos h " +
        "GROUP BY e.categoria " +
        "ORDER BY COUNT(DISTINCT h.id) DESC " +
        "LIMIT 1")
    Categoria findCategoriaConMasHechos();

    // UNA provincia con más hechos por categoría
    @Query("SELECT e.provincia " +
        "FROM EstadisticaCombinacion e " +
        "JOIN e.hechos h " +
        "WHERE e.categoria.id = :idCategoria " +
        "GROUP BY e.provincia " +
        "ORDER BY COUNT(DISTINCT h.id) DESC " +
        "LIMIT 1")
    Provincia findProvinciaConMasHechosPorCategoria(@Param("idCategoria") Long idCategoria);

    // UNA hora con más hechos por categoría
    @Query("SELECT e.hora " +
        "FROM EstadisticaCombinacion e " +
        "JOIN e.hechos h " +
        "WHERE e.categoria.id = :idCategoria " +
        "GROUP BY e.hora " +
        "ORDER BY COUNT(DISTINCT h.id) DESC " +
        "LIMIT 1")
    HoraDelDia findHoraConMasHechosPorCategoria(@Param("idCategoria") Long idCategoria);
}