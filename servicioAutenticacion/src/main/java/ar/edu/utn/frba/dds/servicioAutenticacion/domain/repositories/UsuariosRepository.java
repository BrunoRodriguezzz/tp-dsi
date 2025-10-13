package ar.edu.utn.frba.dds.servicioAutenticacion.domain.repositories;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<Usuario> findByUsernameIgnoreCase(@Param("username") String username);
}
