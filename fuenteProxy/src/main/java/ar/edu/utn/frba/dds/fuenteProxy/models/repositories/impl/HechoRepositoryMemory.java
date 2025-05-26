package ar.edu.utn.frba.dds.fuenteProxy.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteProxy.models.domain.FiltroProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.HechoProxy;
import ar.edu.utn.frba.dds.fuenteProxy.models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechoRepositoryMemory implements IHechoRepository {
    private final Map<Long, HechoProxy> hechos = new HashMap<>();  // guarda entidades
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<HechoProxy> getAll() {
        return new ArrayList<>(hechos.values());
    }

    @Override
    public void delete(Long idHecho) {
        if (idHecho != null && idHecho >= 1) { //TODO Va la validación acá
            this.hechos.remove(idHecho);
        }
    }

    @Override
    public List<HechoProxy> getWithFilters(FiltroProxy filtro){
        List<HechoProxy> hechosFiltrados = this.getAll();         // Hay q inicializar --> en el seeder

        if(filtro.getIdHecho() != null) hechosFiltrados.stream()
                .filter(h -> h.getId().equals(filtro.getIdHecho()))
                .toList();

        if(filtro.getFecha() != null) hechosFiltrados.stream()
                .filter(hechoProxy -> hechoProxy.getFechaModificacion().equals(filtro.getFecha()))
                .toList();

        if(filtro.getFuenteId() != null) hechosFiltrados.stream()
                .filter(h -> h.getIdFuente().equals(filtro.getFuenteId()))
                .toList();

        return hechosFiltrados;
    }

    @Override
    public List<HechoProxy> getByIdFuente(Long idFuente){
        List<HechoProxy> hechos = this.getAll();
        return hechos.stream().filter(h -> h.getIdFuente().equals(idFuente)).toList();
    }

    @Override
    public HechoProxy getById(Long id){
        return hechos.get(id);
    }

    @Override
    public void guardarHecho(HechoProxy hecho) {
        hecho.setId(idGenerator.getAndIncrement());
        //hecho.setNombreFuente
        hechos.put(hecho.getId(), hecho);
    }

    @Override
    public List<HechoProxy> getFiltrados(Long idFuente, FiltroProxy filtro) {
        List<HechoProxy> hechos = this.getByIdFuente(idFuente);
        return hechos.stream().filter(filtro::cumple).toList();
    }
}