package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArchivoRepositoryMemory implements IArchivoRepository {
    private final Map<Long, Archivo> archivos;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ArchivoRepositoryMemory(List<Archivo> archivos) {
        this.archivos = new HashMap<>();
        archivos.forEach(archivo -> {
            this.archivos.put(this.idGenerator.getAndIncrement(), archivo);
        });
    }

    @Override
    public List<Archivo> getAll() {
        return archivos.values().stream().toList();
    }

}
