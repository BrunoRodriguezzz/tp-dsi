package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IDinamicaRepository extends JpaRepository<Hecho,Long> {
    List<Hecho> findAllByContribuyente_IdUsuario(Long idUsuario);

    List<Hecho> findByEstadoHecho(EstadoHecho estadoHecho);
}
