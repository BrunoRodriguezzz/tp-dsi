package ar.edu.utn.frba.dds.agregador.schedulers;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.normalizador.CategoriaSinonimo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaSinonimoRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.IHechoService;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatNormScheduler {
    private static final double UMBRAL_SIMILARIDAD = 0.85;
    private final JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
    private final ICategoriaRepository categoriaRepository;
    private final ICategoriaSinonimoRepository categoriaSinonimoRepository;
    private final IHechoRepository hechoRepository;
    private final IHechoService hechoService;

    public CatNormScheduler(ICategoriaRepository categoriaRepository, ICategoriaSinonimoRepository categoriaSinonimoRepository, IHechoRepository hechoRepository, IHechoService hechoService) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaSinonimoRepository = categoriaSinonimoRepository;
        this.hechoRepository = hechoRepository;
        this.hechoService = hechoService;
    }

    @Scheduled(cron = "0 0 0 * * 0")
    public void buscarYAgregarSinonimos() {
        List<Categoria> categoriasCambiadas = new ArrayList<>();
        List<CategoriaSinonimo> sinonimosExistentes = categoriaSinonimoRepository.findAll();
        categoriaRepository
                .findCategoriasSinSinonimos()
                .forEach(categoria -> {
                    double resultadoComparacion = 0.0;
                    CategoriaSinonimo sinonimoCandidato = null;
                    for (CategoriaSinonimo cs : sinonimosExistentes) {
                        double aux = this.similarity.apply(categoria.getTitulo(), cs.getSinonimo());
                        if (aux > resultadoComparacion) {
                            resultadoComparacion = aux;
                            sinonimoCandidato = cs;
                        }
                    }
                    if (resultadoComparacion > UMBRAL_SIMILARIDAD) {
                        categoriasCambiadas.add(categoria);
                        CategoriaSinonimo nuevoSin = new CategoriaSinonimo(sinonimoCandidato.getSinonimo(), categoria);
                        categoriaSinonimoRepository.save(nuevoSin);
                    }
                });

        List<Hecho> hechosANormalizar = new ArrayList<>();
        categoriasCambiadas.forEach(cat -> hechosANormalizar.addAll(this.hechoRepository.findByCategoria(cat)));
        this.hechoService.guardarHechos(Flux.fromIterable(hechosANormalizar)).blockLast();
    }
}
