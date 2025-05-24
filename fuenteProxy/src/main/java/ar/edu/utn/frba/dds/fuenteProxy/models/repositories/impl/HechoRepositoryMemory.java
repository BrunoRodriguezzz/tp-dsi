package ar.edu.utn.frba.dds.fuenteProxy.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechoRepositoryMemory implements IHechoRepository {
    private final Map<Long, HechoProxy> hechos = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<HechoProxy> getAll() {
        return new ArrayList<>(hechos.values());
    }

    @Override
    public HechoProxy getById(Long id) {
        return this.hechos.get(id);
    }

    @Override
    public void save(HechoProxy hecho) {
        if(hecho.getId() == null) {
            Long id = idGenerator.getAndIncrement();
            hecho.setId(id);
            this.hechos.put(id, hecho);
        } else {// PUT
            this.hechos.put(hecho.getId(), hecho);
        }
    }

    @Override
    public void delete(HechoProxy hecho) {
        if (hecho.getId() != null) {
            this.hechos.remove(hecho.getId());
        }
    }
}