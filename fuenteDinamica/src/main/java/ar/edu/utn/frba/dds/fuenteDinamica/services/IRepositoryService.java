package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IRepositoryService {
    public List<Hecho> buscarTodos();
    public Hecho       buscarPorID (Long ID);
    public void        guardar(Hecho hecho);
    public List<Hecho> mostrarTodos(LocalDateTime filtro);
    public List<Hecho> mostrarEnviados(Boolean enviado,LocalDateTime filtro);
    public List<Hecho> buscarTodosDeUnUsuario(Long idUsuario);

    List<Hecho> buscarPendientes();
}