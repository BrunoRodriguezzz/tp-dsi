package ar.edu.utn.frba.dds.agregador.services.impl.normalizador;

import ar.edu.utn.frba.dds.agregador.models.domain.comparador.ComparadorHechos;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
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
        return this.buscarCandidatosReactivo(hecho).block();
    }

    @Override
    public Flux<Hecho> normalizarHechosReactivo(Flux<Hecho> hechos) {
        return hechos
                .publishOn(Schedulers.boundedElastic())
                .flatMap(hecho ->
                        Mono.just(hecho)
                                .flatMap(this::normalizarCategoriaReactivo)
                                .flatMap(this::buscarCandidatosReactivo)
                                .onErrorResume(e -> Mono.just(hecho)),
                5
                );
    }

    private Mono<Hecho> normalizarCategoriaReactivo(Hecho hecho) {
        return Mono.fromCallable(() ->
                        normalizadorCategoriaService.normalizarCategoria(
                                hecho.getCategoria().getTitulo()
                        )
                )
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(categoriaNormalizada ->
                        Mono.fromCallable(() ->
                                        categoriaRepository.findByTituloOrCreate(categoriaNormalizada)
                                )
                                .subscribeOn(Schedulers.boundedElastic())
                )
                .map(categoriaGestionada -> {
                    hecho.setCategoria(categoriaGestionada);
                    return hecho;
                });
    }


    private Mono<Hecho> buscarCandidatosReactivo(Hecho hecho) {
        LocalDateTime fecha = hecho.getFechaAcontecimiento();
        LocalDateTime desde = fecha.minusDays(1);
        LocalDateTime hasta = fecha.plusDays(1);

        double lat = hecho.getUbicacion().getLatitud();
        double lon = hecho.getUbicacion().getLongitud();
        double delta = 0.05;

        return Mono.fromCallable(() ->
                        hechoRepository.findByFechaYUbicacion(
                                desde, hasta,
                                lat - delta, lat + delta,
                                lon - delta, lon + delta
                        )
                )
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .filter(candidato -> comparadorHechos.comparar(hecho, candidato))
                .next()
                .map(candidato -> {
                    hecho.getFuenteSet()
                            .stream()
                            .findFirst()
                            .ifPresent(hf -> candidato.agregarFuente(hf.getFuente(), hf.getIdInternoFuente()));
                    return candidato;
                })
                .defaultIfEmpty(hecho);
    }
}