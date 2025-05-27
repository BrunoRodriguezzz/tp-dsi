package ar.edu.utn.frba.dds.fuenteEstatica.models.repositories.impl;

import ar.edu.utn.frba.dds.domain.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.domain.models.entities.valueObjectsHecho.Origen;
import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
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

    public HechoRepositoryMemory(FuenteEstatica fuenteEstatica) {
        this.hechos = new HashMap<>();
    }

    @Override
    public List<HechoEstatica> findAll(Archivo archivo) {
        // return fuenteEstatica.importarHechos().stream().map(this::hechoToHechoEstatica).toList();
        // List<Hecho> hechos = archivo.stream().flatMap(archivo -> archivo.importarHechos().stream()).toList();
        List<Hecho> hechos = archivo.importarHechos().stream().toList();
        hechos.forEach(hecho -> {
            Long id = this.idGenerator.getAndIncrement();
            this.hechos.put(id, this.hechoToHechoEstatica(hecho, id));
        });
        return this.hechos.values().stream().filter(hechoEstatica -> !hechoEstatica.getEliminado()).toList();
    }

    @Override
    public HechoEstatica getById(Long id) {
       return Optional
                .ofNullable(this.hechos.get(id))
                .orElseThrow(()-> new NoSuchElementException("No se encontró el hecho con id = " + id));
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
    public void delete(Long id) {
        this.hechos.get(id).setEliminado(true);
    }

    private HechoEstatica hechoToHechoEstatica(Hecho hecho, Long id){
        HechoEstatica hechoEstatico = new HechoEstatica();
        hechoEstatico.setTitulo(hecho.getTitulo());
        hechoEstatico.setDescripcion(hecho.getDescripcion());
        hechoEstatico.setCategoria(hecho.getCategoria().getTitulo());
        // TODO: en el dominio está como un string
        hechoEstatico.setLatitud(Double.parseDouble(hecho.getUbicacion().getLatitud()));
        hechoEstatico.setLongitud(Double.parseDouble(hecho.getUbicacion().getLongitud()));
        if (hecho.getFechaAcontecimiento() != null) hechoEstatico.setFechaHecho(hecho.getFechaAcontecimiento());
        // TODO: en el Dominio está la fecha de Carga como LocalDate
        if (hecho.getFechaCarga() != null) hechoEstatico.setFechaCreacion(LocalDateTime.parse(hecho.getFechaCarga().toString()));
        hechoEstatico.setEliminado(hecho.getEstaEliminado());
        hechoEstatico.setOrigen(Origen.DATASET);
        // TODO: idFuente?? -> es el idArchivo
        return hechoEstatico;
    }
}
