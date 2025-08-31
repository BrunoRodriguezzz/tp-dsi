package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IArchivoRepository extends JpaRepository<Archivo, Long> {
//    public List<Archivo> getAll();
//    public void save(Archivo archivo);
//    public List<Long> devolverIDs();
//    public Archivo getById(Long id);
}
