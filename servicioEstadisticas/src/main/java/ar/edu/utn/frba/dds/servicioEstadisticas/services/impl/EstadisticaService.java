package ar.edu.utn.frba.dds.servicioEstadisticas.services.impl;

import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Categoria;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.models.Provincia;
import ar.edu.utn.frba.dds.servicioEstadisticas.domain.repositories.IEstadisticaRepository;
import ar.edu.utn.frba.dds.servicioEstadisticas.services.IEstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class EstadisticaService implements IEstadisticaService {
    @Autowired
    private IEstadisticaRepository estadisticaRepository;

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
}
