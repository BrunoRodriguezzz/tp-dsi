package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.colecciones.Coleccion;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
  List<Coleccion> findColeccionsByHechos(List<Hecho> hechos);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO hecho_x_coleccion (coleccion_id, hecho_id) VALUES (:coleccionId, :hechoId)", nativeQuery = true)
    void insertHechoEnColeccion(@Param("coleccionId") Long coleccionId, @Param("hechoId") Long hechoId);

    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM Coleccion c JOIN c.hechos h WHERE c.id = :coleccionId AND h.id = :hechoId")
    boolean existsHechoInColeccion(@Param("coleccionId") Long coleccionId, @Param("hechoId") Long hechoId);

}
