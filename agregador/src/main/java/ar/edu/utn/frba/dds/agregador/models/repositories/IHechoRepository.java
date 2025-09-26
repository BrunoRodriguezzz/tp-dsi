package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.Fuente;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Origen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findByOrigen(Origen origen);

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

    @Query("SELECT DISTINCT h FROM Hecho h " +
            "JOIN h.fuenteSet hf " +
            "WHERE hf.fuente = :fuente")
    List<Hecho> findByFuente(@Param("fuente") Fuente fuente);

    @Query("SELECT h FROM Hecho h " +
            "WHERE h.fechaAcontecimiento BETWEEN :desde AND :hasta " +
            "AND h.ubicacion.latitud BETWEEN :latMin AND :latMax " +
            "AND h.ubicacion.longitud BETWEEN :lonMin AND :lonMax")
    List<Hecho> findByFechaYUbicacion(LocalDateTime desde, LocalDateTime hasta,
                                         double latMin, double latMax,
                                         double lonMin, double lonMax);

//  public Boolean inicializarHechos(List<Hecho> hechos);
//  public List<Hecho> buscarHechosGuardadosFuente(Fuente fuente);
}
