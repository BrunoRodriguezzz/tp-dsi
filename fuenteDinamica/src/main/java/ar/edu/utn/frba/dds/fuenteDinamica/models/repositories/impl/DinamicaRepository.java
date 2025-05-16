package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.impl;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DinamicaRepository implements IDinamicaRepository {

    private List<Hecho> hechos;

    public DinamicaRepository(){
        this.hechos = new ArrayList<>();
    }

    @Override
    public Hecho buscarPorID(Long ID) {
        return null;
    }

    @Override
    public void guardar(Hecho hecho) {
        hecho.setId((long) this.hechos.size());
        hecho.setFechaCarga(LocalDate.now());
        this.hechos.add(hecho);
    }

    @Override
    public void eliminar(Hecho hecho) {
        // TODO: Revisar enunciado, ¿puede eliminarse por solicitud o decision del Administrador?
    }

    @Override
    public List<Hecho> mostrarTodos() {
        return this.hechos;
    }
}