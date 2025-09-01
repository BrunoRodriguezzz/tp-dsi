package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContribuyenteRepository extends JpaRepository<Contribuyente,Long> {
    /*public void    guardar(Contribuyente contribuyente);
    public Boolean comprobarUsuarioRegistrado(Contribuyente contribuyente);*/
}
