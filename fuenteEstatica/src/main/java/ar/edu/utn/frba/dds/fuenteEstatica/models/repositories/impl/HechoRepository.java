package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.dto.HechoDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechoRepository implements IHechoRepository {
    private final Map<Long, HechoEstatica> hechos = new HashMap<>();  // guarda entidades
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<HechoEstatica> findAll() {
        return new ArrayList<>(hechos.values());
    }
}
