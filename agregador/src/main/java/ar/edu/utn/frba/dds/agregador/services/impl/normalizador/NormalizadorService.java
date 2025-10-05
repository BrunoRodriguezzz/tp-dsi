package ar.edu.utn.frba.dds.agregador.services.impl.normalizador;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.ValidationException;
import ar.edu.utn.frba.dds.agregador.models.domain.comparador.ComparadorHechos;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.HechoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.INormalizadorCategoriaService;
import ar.edu.utn.frba.dds.agregador.services.INormalizadorService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NormalizadorService implements INormalizadorService {
    private INormalizadorCategoriaService normalizadorCategoriaService;
    private IHechoRepository hechoRepository;
    private ComparadorHechos comparadorHechos;
    private ICategoriaRepository categoriaRepository;

    public NormalizadorService(INormalizadorCategoriaService normalizadorCategoriaService, IHechoRepository hechoRepository, ICategoriaRepository categoriaRepository) {
        this.normalizadorCategoriaService = normalizadorCategoriaService;
        this.hechoRepository = hechoRepository;
        this.categoriaRepository = categoriaRepository;
        this.comparadorHechos = new ComparadorHechos();
    }

    @Deprecated
    @Override
    public Hecho normalizarHecho(Hecho hecho) {
        String nombreCatNormalizado = normalizadorCategoriaService.normalizarCategoria(hecho.getCategoria().getTitulo());
        Categoria categoriaNormalizada = this.categoriaRepository.findByTituloOrCreate(nombreCatNormalizado);
        hecho.setCategoria(categoriaNormalizada);

        return this.buscarCandidatos(hecho).block();
    }

    @Override
    public Flux<Hecho> normalizarHechosReactivo(Flux<Hecho> hechos) {
        return hechos.flatMap(hecho ->
                Mono.fromCallable(() -> normalizadorCategoriaService.normalizarCategoria(hecho.getCategoria().getTitulo()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(categoriaNormalizada ->
                    // Buscar o crear la categoría de forma reactiva
                    Mono.fromCallable(() -> categoriaRepository.findByTituloOrCreate(categoriaNormalizada))
                            .subscribeOn(Schedulers.boundedElastic())
                            .map(categoriaGestionada -> {
                                hecho.setCategoria(categoriaGestionada);
                                return hecho;
                            })
                )
                .flatMap(this::buscarCandidatos), 50 // Concurrencia máxima
        );
    }

    private Mono<Hecho> buscarCandidatos(Hecho hechoConCategoria) {
        LocalDateTime fecha = hechoConCategoria.getFechaAcontecimiento();
        LocalDateTime desde = fecha.minusDays(1);
        LocalDateTime hasta = fecha.plusDays(1);

        double lat = hechoConCategoria.getUbicacion().getLatitud();
        double lon = hechoConCategoria.getUbicacion().getLongitud();
        double delta = 0.05;

        double latMin = lat - delta;
        double latMax = lat + delta;
        double lonMin = lon - delta;
        double lonMax = lon + delta;

        return buscarCandidatosReactivo(desde, hasta, latMin, latMax, lonMin, lonMax)
                .collectList()
                .flatMapMany(candidatos -> Flux.fromIterable(candidatos)
                        .filter(h -> this.comparadorHechos.comparar(hechoConCategoria, h)))
                .next()
                .map(h -> {
                    HechoFuente hf = hechoConCategoria.getFuenteSet()
                            .stream()
                            .findFirst()
                            .orElse(null);
                    if (hf == null) {
                        throw new ValidationException("Me llego un hecho sin fuente");
                    }
                    h.agregarFuente(hf.getFuente(), hf.getIdInternoFuente());
                    return h;
                })
                .onErrorResume(ValidationException.class, e -> {
                    // Si no hay fuente, devolver el hecho original
                    return Mono.just(hechoConCategoria);
                })
                .defaultIfEmpty(hechoConCategoria);
    }

    // Método auxiliar para buscar candidatos de forma reactiva
    private Flux<Hecho> buscarCandidatosReactivo(LocalDateTime desde, LocalDateTime hasta,
                                                 double latMin, double latMax,
                                                 double lonMin, double lonMax) {
        return Flux.defer(() -> Flux.fromIterable(
                hechoRepository.findByFechaYUbicacion(desde, hasta, latMin, latMax, lonMin, lonMax)
        )).subscribeOn(Schedulers.boundedElastic());
    }
}
