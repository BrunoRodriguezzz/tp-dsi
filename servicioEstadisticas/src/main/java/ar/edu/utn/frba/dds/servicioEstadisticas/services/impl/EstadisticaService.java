package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.*;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.IEstadisticaHechosRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.ISolicitudRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IEstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class EstadisticaService implements IEstadisticaService {
    @Autowired
    private IEstadisticaHechosRepository estadisticaRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IColeccionRepository coleccionRepository;

    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Override
    public Provincia provinciaConMasHechosDeUnaColeccion(Long idColeccion) {
        // TODO: Implementación
        return null;
    }

    @Override
    public Categoria categoriaConMasHechos() {
        // TODO: Implementación
        return null;
    }

    @Override
    public Provincia provinciaConMasHechosSegunCategoria(Long idCategoria) {
        // TODO: Implementación
        return null;
    }

    @Override
    public LocalTime horaConMasHechosSegunCategoria(Long idCategoria) {
        // TODO: Implementación
        return null;
    }

    @Override
    public Integer cantSolicitudesSpam() {
        // TODO: Implementación
        return 0;
    }

    @Override
    public EstadisticaHechos crearEstadistica(EstadisticaHechos estadistica) {
        Categoria categoriaGuardada = this.findOrCreate(estadistica.getCategoria());
        Coleccion coleccionGuardada = this.findOrCreate(estadistica.getColeccion());

        estadistica.setCategoria(categoriaGuardada);
        estadistica.setColeccion(coleccionGuardada);

        return this.estadisticaRepository.save(estadistica);
    }

    private Coleccion findOrCreate(Coleccion coleccion) {
        return coleccionRepository.findByDetalle(coleccion.getDetalle())
                .orElseGet(() -> {
                    return coleccionRepository.save(coleccion);
                });
    }

    private Categoria findOrCreate(Categoria categoria) {
        return categoriaRepository.findByDetalle(categoria.getDetalle())
                .orElseGet(() -> {
                    return categoriaRepository.save(categoria);
                });
    }

    private Solicitud findOrCreate(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }
}
