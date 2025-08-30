package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;

public interface IRepositoryService {
    public Hecho       buscarPorID (Long ID);
    public void        guardar(Hecho hecho);
    public List<Hecho> mostrarTodos(LocalDateTime filtro);
    public List<Hecho> mostrarEnviados(Boolean enviado,LocalDateTime filtro);
    public void        guardarCambios(Hecho hechoOriginal, Hecho hechoCambiado);
}
