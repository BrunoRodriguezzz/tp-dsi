package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {

    Page<Hecho> findAll(Specification<Hecho> spec, Pageable pageable);


    @Query("SELECT h FROM Hecho h " +
            "JOIN h.fuenteSet hf " +
            "WHERE hf.fuente.id = :fuenteId " +
            "AND hf.idInternoFuente = :idInternoFuente")
    Optional<Hecho> findByFuenteIdAndIdInternoFuente(
            @Param("fuenteId") Long fuenteId,
            @Param("idInternoFuente") Long idInternoFuente);

    @Query("SELECT DISTINCT h FROM Hecho h " +
            "JOIN h.fuenteSet hf " +
            "WHERE hf.fuente IN :fuentes")
    List<Hecho> findByFuentes(@Param("fuentes") List<Fuente> fuentes);

    @Query("SELECT h FROM Hecho h " +
            "WHERE h.fechaAcontecimiento BETWEEN :desde AND :hasta " +
            "AND h.ubicacion.latitud BETWEEN :latMin AND :latMax " +
            "AND h.ubicacion.longitud BETWEEN :lonMin AND :lonMax")
    List<Hecho> findByFechaYUbicacion(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            @Param("latMin") double latMin,
            @Param("latMax") double latMax,
            @Param("lonMin") double lonMin,
            @Param("lonMax") double lonMax);

    List<Hecho> findByCategoria(Categoria categoria);

    @Query("""
        SELECT h.id 
        FROM Hecho h 
        WHERE h.estaEliminado = false 
        AND EXISTS (
            SELECT 1 
            FROM Coleccion c 
            JOIN c.hechos ch 
            WHERE c.id = :coleccionId 
            AND ch.id = h.id
        )
        ORDER BY h.id DESC
        """)
    Page<Long> findIdsByColeccionId(@Param("coleccionId") Long coleccionId, Pageable pageable);

    @Query("""
        SELECT DISTINCT h 
        FROM Hecho h 
        LEFT JOIN FETCH h.categoria 
        LEFT JOIN FETCH h.ubicacion 
        LEFT JOIN FETCH h.contribuyente 
        LEFT JOIN FETCH h.fuenteSet fs 
        LEFT JOIN FETCH fs.fuente 
        WHERE h.id IN :ids
        ORDER BY h.id DESC
        """)
    List<Hecho> findByIdsWithJoinFetch(@Param("ids") List<Long> ids);

    @Query("""
        SELECT h
        FROM Hecho h
        WHERE h.id NOT IN (
            SELECT h2.id
            FROM Coleccion c JOIN c.hechos h2
        ) AND h.estaEliminado = false
    """)
    List<Hecho> buscarHechosIndependientes();

    @Query(value = "SELECT fecha_acontecimiento FROM hecho WHERE id = :id", nativeQuery = true)
    Object getFechaRaw(@Param("id") Long id);
}
