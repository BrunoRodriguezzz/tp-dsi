package ar.edu.utn.frba.dds.agregador.models.repositories;

import ar.edu.utn.frba.dds.agregador.models.domain.normalizador.CategoriaSinonimo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaSinonimoRepository extends JpaRepository<CategoriaSinonimo, Long> {
  @Query("SELECT cs FROM CategoriaSinonimo cs WHERE LOWER(cs.sinonimo) = LOWER(:sinonimo)")
  Optional<CategoriaSinonimo> findBySinonimoIgnoreCase(@Param("sinonimo") String sinonimo);

  List<CategoriaSinonimo> findByCategoria(Categoria categoria);

  boolean existsBySinonimo(String sinonimo);

  @Query("SELECT cs.categoria FROM CategoriaSinonimo cs WHERE LOWER(cs.sinonimo) = LOWER(:sinonimo)")
  Optional<Categoria> findCategoriaBySinonimo(@Param("sinonimo") String sinonimo);
}
