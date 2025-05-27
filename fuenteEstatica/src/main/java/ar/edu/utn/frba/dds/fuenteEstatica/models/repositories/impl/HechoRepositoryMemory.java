package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class HechoRepositoryMemory implements IHechoRepository {
    private final Map<Long, HechoEstatica> hechos;  // guarda entidades
    private final AtomicLong idGenerator = new AtomicLong(1);

    public HechoRepositoryMemory() {
        this.hechos = new HashMap<>();
    }

    @Override
    public List<HechoEstatica> getAll() {
        return new ArrayList<>(hechos.values());
    }

    @Override
    public HechoEstatica getById(Long id) {
       return Optional
                .ofNullable(this.hechos.get(id))
                .orElseThrow(()-> new NoSuchElementException("No se encontró el hecho con id = " + id));
    }

    @Override
    public List<HechoEstatica> getByIdArchivo(Long id) { //TODO Validaciones
        List<HechoEstatica> hechos = this.getAll();
        return hechos.stream().filter(h -> h.getIdArchivo().equals(id)).toList();
    }

    @Override
    public HechoEstatica save(HechoEstatica hechoEstatica) {
        if (hechoEstatica.getId() == null) {
            // Nuevo
            Long id = idGenerator.getAndIncrement();
            hechoEstatica.setId(id);
            this.hechos.put(id, hechoEstatica);
        } else {
            // Actualización
            this.hechos.put(hechoEstatica.getId(), hechoEstatica);
        }
        return hechoEstatica;
    }

    @Override
    public void delete(Long id) { // TODO validaciones
        this.hechos.get(id).eliminar();
    }


}
