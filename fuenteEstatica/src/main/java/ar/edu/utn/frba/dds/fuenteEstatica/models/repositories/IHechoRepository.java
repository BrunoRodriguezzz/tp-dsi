package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.output.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IHechoRepository extends JpaRepository<HechoEstatica, Long>, JpaSpecificationExecutor<HechoEstatica> {
    List<HechoEstatica> findByIdArchivo(Long id);

    List<HechoEstatica> findByTitulo(String titulo);
//    List<HechoEstatica> getAll();
//    HechoEstatica getById(Long id);
//    HechoEstatica save(HechoEstatica hechoEstatica);
//    public List<HechoEstatica> getByIdArchivo(Long id);
//    void delete(Long id);
//    public List<HechoEstatica> getFiltrados(Long idFuente, FiltroEstatica filtro);
//    public void guardarHecho(HechoEstatica hecho);
//    public List<HechoEstatica> getByName(String title);
}
