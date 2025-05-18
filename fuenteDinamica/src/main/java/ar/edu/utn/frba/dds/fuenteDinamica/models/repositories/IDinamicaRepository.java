package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;

import java.util.List;

public interface IDinamicaRepository {

    public Hecho       buscarPorID (Long ID);
    public void        guardar(Hecho hecho);
    public void        eliminar(Hecho hecho);
    public List<Hecho> mostrarTodos();
    public List<Hecho> mostrarEnviados(Boolean enviado);
}
