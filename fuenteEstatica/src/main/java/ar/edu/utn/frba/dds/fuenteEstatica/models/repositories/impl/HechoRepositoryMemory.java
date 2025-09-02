package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.NotFoundError;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.FiltroEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.HechoEstatica;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

//@Repository
//public class HechoRepositoryMemory implements IHechoRepository {
//    private final Map<Long, HechoEstatica> hechos;  // guarda entidades
//    private final AtomicLong idGenerator = new AtomicLong(1);
//
//    public HechoRepositoryMemory() {
//        this.hechos = new HashMap<>();
//    }
//
//    @Override
//    public List<HechoEstatica> getAll() {
//        List<HechoEstatica> hechos = new ArrayList<>(this.hechos.values());
//        return hechos.stream().filter(hecho -> hecho.getEliminado().equals(false)).toList();
//    }
//
//    @Override
//    public HechoEstatica getById(Long id) { //TODO Validar que no este eliminado
//        return hechos.get(id);
//    }
//
//    @Override
//    public List<HechoEstatica> getByIdArchivo(Long id) { //TODO Validaciones
//        List<HechoEstatica> hechos = this.getAll();
//        return hechos.stream().filter(h -> h.getIdArchivo().equals(id)).toList();
//    }
//
//    @Override
//    public HechoEstatica save(HechoEstatica hechoEstatica) {
//        if (hechoEstatica.getId() == null) {
//            // Nuevo
//            Long id = idGenerator.getAndIncrement();
//            hechoEstatica.setId(id);
//            hechoEstatica.setEliminado(false);
//            this.hechos.put(id, hechoEstatica);
//        } else {
//            // Actualización
//            this.hechos.put(hechoEstatica.getId(), hechoEstatica);
//        }
//        return hechoEstatica;
//    }
//
//    @Override
//    public void delete(Long idHecho) {
//        HechoEstatica hecho = hechos.get(idHecho);
//        if (hecho == null) {
//            throw new NotFoundError("No existe hecho con ID: " + idHecho);
//        }
//
//        hecho.setEliminado(true);
//    }
//
//    @Override
//    public List<HechoEstatica> getFiltrados(Long idFuente, FiltroEstatica filtro) {
//        List<HechoEstatica> hechos = this.getByIdArchivo(idFuente);
//        hechos = hechos.stream() // Saco los eliminados
//                .filter(h -> h.getEliminado().equals(false))
//                .toList();
//        hechos = hechos.stream() // Aplico el filtro
//                .filter(filtro::cumple)
//                .toList();
//        return hechos;
//    }
//
//    @Override
//    public void guardarHecho(HechoEstatica hecho) {
//        hecho.setId(idGenerator.getAndIncrement());
//        hechos.put(hecho.getId(), hecho);
//    }
//
//    @Override
//    public List<HechoEstatica> getByName(String title) {
//        return this.hechos.values()
//            .stream()
//            .filter(h -> h.getTitulo().equals(title))
//            .toList();
//    }
//}
