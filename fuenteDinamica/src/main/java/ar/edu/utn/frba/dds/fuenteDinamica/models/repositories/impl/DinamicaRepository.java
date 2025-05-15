package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;

import java.util.List;

public class DinamicaRepository implements IDinamicaRepository {

    private List<Hecho> hechos;

    @Override
    public Hecho buscarPorID(Long ID) {
        return null;
    }

    @Override
    public void guardar(Hecho hecho) {

    }

    @Override
    public void eliminar(Hecho hecho) {

    }

    @Override
    public List<Hecho> mostrarTodos() {
        return List.of();
    }
}
