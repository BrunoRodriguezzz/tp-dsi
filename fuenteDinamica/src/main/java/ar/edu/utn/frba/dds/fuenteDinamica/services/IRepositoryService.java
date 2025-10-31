package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoModificadoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.SolicitudModificacion;

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
    void guardarSolicitudModificacion(SolicitudModificacion hecho);
    SolicitudModificacion buscarSolicitud(Long idSolicitud);
    List<SolicitudModificacion> buscarSolicitudesPendientes();

    List<Hecho> buscarPendientes();
    Hecho buscarPendientesPorID(Long id);
    List<Hecho> buscarTodosDeUnContribuyente(Long idContribuyente);
}