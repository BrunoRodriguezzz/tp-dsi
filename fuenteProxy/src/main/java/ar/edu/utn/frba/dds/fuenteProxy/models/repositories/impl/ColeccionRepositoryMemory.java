package ar.edu.utn.frba.dds.fuenteProxy.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.Coleccion;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IColeccionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ColeccionRepositoryMemory implements IColeccionRepository {
    private final Map<Long, Coleccion> colecciones = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void guardar(Coleccion coleccion) {
        colecciones.put(coleccion.getId(), coleccion);
    }

    @Override
    public List<Coleccion> getAll() {
        return new ArrayList<>(colecciones.values());
    }

    @Override
    public Coleccion getById(Long id) {
        return colecciones.get(id);
    }

    @Override
    public void guardarHecho(Coleccion coleccion) {
        coleccion.setId(idGenerator.getAndIncrement());
        colecciones.put(coleccion.getId(), coleccion);
    }

    @Override
    public void eliminar(Long id) {
        colecciones.remove(id);
    }
}
