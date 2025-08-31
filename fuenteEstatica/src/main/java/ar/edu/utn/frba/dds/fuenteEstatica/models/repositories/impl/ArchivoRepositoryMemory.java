package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.IArchivoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
//public class ArchivoRepositoryMemory implements IArchivoRepository {
//    private final Map<Long, Archivo> archivos = new HashMap<>();
//    private final AtomicLong idGenerator = new AtomicLong(1);
//
//    @Override
//    public List<Archivo> getAll() {
//        return new ArrayList<>(archivos.values());
//    }
//
//    @Override
//    public void save(Archivo archivo) {
//        if(archivo.getId() == null){
//            Long id = idGenerator.getAndIncrement();
//            archivo.setId(id);
//            this.archivos.put(id, archivo);
//        } else {
//            this.archivos.put(archivo.getId(), archivo);
//        }
//    }
//
//    @Override
//    public Archivo getById(Long id) {
//        return this.archivos.get(id);
////        return Optional
////                .ofNullable(this.archivos.get(id))
////                .orElseThrow(()-> new NoSuchElementException("No se encontró el Archivo con id = " + id));
//    }
//
//    public List<Long> devolverIDs() {
//        return new ArrayList<>(archivos.keySet());
//    }
//
//}
