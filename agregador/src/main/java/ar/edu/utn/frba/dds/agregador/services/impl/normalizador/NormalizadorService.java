package ar.edu.utn.frba.dds.agregador.services.impl.normalizador;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.ValidationException;
import ar.edu.utn.frba.dds.agregador.models.domain.comparador.ComparadorHechos;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;
import ar.edu.utn.frba.dds.agregador.models.domain.hechos.HechoFuente;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.agregador.services.INormalizadorCategoriaService;
import ar.edu.utn.frba.dds.agregador.services.INormalizadorService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NormalizadorService implements INormalizadorService {
    private INormalizadorCategoriaService normalizadorCategoriaService;
    private IHechoRepository hechoRepository;
    private ComparadorHechos comparadorHechos;

    public NormalizadorService(INormalizadorCategoriaService normalizadorCategoriaService, IHechoRepository hechoRepository) {
        this.normalizadorCategoriaService = normalizadorCategoriaService;
        this.hechoRepository = hechoRepository;
        ComparadorHechos comparadorHechos = new ComparadorHechos();
    }

    @Override
    public Hecho normalizarHecho(Hecho hecho) {
        String categoriaNormalizada = normalizadorCategoriaService.normalizarCategoria(hecho.getCategoria().getTitulo());
        Categoria categoria = new Categoria(categoriaNormalizada);
        hecho.setCategoria(categoria);

        // Calculamos rangos
        LocalDateTime fecha = hecho.getFechaAcontecimiento();
        LocalDateTime desde = fecha.minusDays(1);
        LocalDateTime hasta = fecha.plusDays(1);

        double lat = Double.parseDouble(hecho.getUbicacion().getLatitud());
        double lon = Double.parseDouble(hecho.getUbicacion().getLongitud());
        double delta = 0.05; // En teoría son unos 5km aprox

        double latMin = lat - delta;
        double latMax = lat + delta;
        double lonMin = lon - delta;
        double lonMax = lon + delta;

        List<Hecho> candidatos = hechoRepository
                .findByFechaYUbicacion(desde, hasta, latMin, latMax, lonMin, lonMax);

        return candidatos.stream()
                .filter(h -> this.comparadorHechos.comparar(hecho, h))
                .findFirst()
                .map(h -> {
                    HechoFuente hf = hecho.getFuenteSet()
                            .stream()
                            .findFirst()
                            .orElse(null);
                    if(hf == null) {
                        throw new ValidationException("Me llego un hecho sin fuente");
                    }
                    h.agregarFuente(hf.getFuente(), hf.getIdInternoFuente());
                    return h;
                })
                .orElse(hecho);
    }
}
