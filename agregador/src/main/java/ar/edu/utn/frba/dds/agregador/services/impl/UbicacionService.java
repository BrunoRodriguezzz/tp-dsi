package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters.IAdapUbicacion;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.HechoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IUbicacionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UbicacionService implements IUbicacionService {
    private IHechoRepository hechoRepository;

    public UbicacionService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

    @Override
    public Hecho cargarUbicacion(Hecho hecho) {
        HechoFuente hf = hecho.getFuenteSet().stream().findFirst().orElse(null);
        if (hf == null) throw new RuntimeException("Tengo un hecho sin fuentes");

        return this.hechoRepository
                .findByFuenteIdAndIdInternoFuente(hf.getFuente().getId(), hf.getIdInternoFuente())
                .map(h -> {
                    hecho.setUbicacion(h.getUbicacion());
                    return hecho;
                })
                .orElseGet(() -> {
                    Ubicacion antigua = hecho.getUbicacion();
                    Ubicacion ubicacion = IAdapUbicacion.buscarUbicacion(String.valueOf(antigua.getLatitud()), String.valueOf(antigua.getLongitud()));
                    hecho.setUbicacion(ubicacion);
                    return hecho;
                });
    }

    @Override
    public Flux<Hecho> obtenerUbicacionesReactivo(Flux<Hecho> hechos) {
        return hechos
                .flatMap(this::buscarUbicacionEnBD)
                .buffer(50)
                .flatMap(chunk -> {
                    List<Hecho> conUbicacion = new ArrayList<>();
                    List<Hecho> sinUbicacion = new ArrayList<>();

                    for (Hecho h : chunk) {
                        if (h.getUbicacion().faltanDatos()) {
                            sinUbicacion.add(h);
                        } else {
                            conUbicacion.add(h);
                        }
                    }

                    Flux<Hecho> hechosProcesados = Flux.empty();
                    if (!sinUbicacion.isEmpty()) {
                        hechosProcesados = IAdapUbicacion.buscarUbicacionesReactivo(sinUbicacion);
                    }

                    return Flux.concat(
                            Flux.fromIterable(conUbicacion),
                            hechosProcesados
                    );
                });
    }

    private Mono<Hecho> buscarUbicacionEnBD(Hecho hecho) {
        HechoFuente hf = hecho.getFuenteSet().stream().findFirst().orElse(null);
        if (hf == null) {
            return Mono.error(new RuntimeException("Tengo un hecho sin fuentes"));
        }

        return this.hechoRepository
                .findByFuenteIdAndIdInternoFuente(hf.getFuente().getId(), hf.getIdInternoFuente())
                .map(h -> {
                    hecho.setUbicacion(h.getUbicacion());
                    return hecho;
                })
                .map(Mono::just)       // Si lo encontró → devolver hecho con ubicación
                .orElse(Mono.just(hecho)); // Si no → devolver el mismo hecho
    }
}