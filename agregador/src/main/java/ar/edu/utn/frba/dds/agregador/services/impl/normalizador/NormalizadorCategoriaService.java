package ar.edu.utn.frba.dds.agregador.services.impl.normalizador;

import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.CategoriaNoEncontradaException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.SinonimoInvalidoException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.SinonimoYaExistenteException;
import ar.edu.utn.frba.dds.agregador.models.domain.normalizador.CategoriaSinonimo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaSinonimoRepository;
import ar.edu.utn.frba.dds.agregador.services.INormalizadorCategoriaService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class NormalizadorCategoriaService implements INormalizadorCategoriaService {
  @Autowired
  private ICategoriaRepository categoriaRepository;

  @Autowired
  private ICategoriaSinonimoRepository categoriaSinonimoRepository;

  private Map<String, String> cacheNormalizacion = new HashMap<>();

  public List<String> normalizarCategorias(List<String> categorias) {
    if (categorias == null || categorias.isEmpty()) {
      return new ArrayList<>();
    }

    return categorias.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(categoria -> !categoria.isEmpty())
        .map(this::normalizarCategoria)
        .collect(Collectors.toList());
  }

  public String normalizarCategoria(String categoria) {
    if (!StringUtils.hasText(categoria)) {
      return categoria;
    }

    String categoriaLimpia = limpiarTexto(categoria);

    // Verificar cache primero
    if (cacheNormalizacion.containsKey(categoriaLimpia)) {
      return cacheNormalizacion.get(categoriaLimpia);
    }

    // Buscar categoría directamente
    Optional<Categoria> categoriaEncontrada =
        categoriaRepository.findByTituloIgnoreCase(categoriaLimpia);

    if (categoriaEncontrada.isPresent()) {
      String tituloNormalizado = categoriaEncontrada.get().getTitulo();
      cacheNormalizacion.put(categoriaLimpia, tituloNormalizado);
      return tituloNormalizado;
    }

    // Buscar en sinónimos
    Optional<Categoria> categoriaDelSinonimo =
        categoriaSinonimoRepository.findCategoriaBySinonimo(categoriaLimpia);

    if (categoriaDelSinonimo.isPresent()) {
      String tituloNormalizado = categoriaDelSinonimo.get().getTitulo();
      cacheNormalizacion.put(categoriaLimpia, tituloNormalizado);
      return tituloNormalizado;
    }

    // Si no se encuentra normalización, devolver original
    cacheNormalizacion.put(categoriaLimpia, categoria);
    return categoria;
  }

  public void agregarSinonimo(Long categoriaId, String sinonimo) {
    validarSinonimo(sinonimo);

    Categoria categoria = categoriaRepository
        .findById(categoriaId)
        .orElseThrow(() -> new CategoriaNoEncontradaException(
            "Categoría con ID " + categoriaId + " no encontrada"));

    String sinonimoLimpio = limpiarTexto(sinonimo);

    // Verificar que no sea igual al título de la categoría
    if (sinonimoLimpio.equalsIgnoreCase(categoria.getTitulo())) {
      throw new SinonimoInvalidoException(
          "El sinónimo no puede ser igual al título de la categoría");
    }

    // Verificar que el sinónimo no exista ya
    if (categoriaSinonimoRepository.existsBySinonimo(sinonimoLimpio)) {
      throw new SinonimoYaExistenteException(
          "El sinónimo '" + sinonimoLimpio + "' ya existe en el sistema");
    }

    // Verificar que no sea una categoría existente
    if (categoriaRepository.existsByTitulo(sinonimoLimpio)) {
      throw new SinonimoInvalidoException(
          "El texto '" + sinonimoLimpio + "' ya es una categoría existente");
    }

    CategoriaSinonimo nuevoSinonimo = new CategoriaSinonimo(sinonimoLimpio, categoria);
    categoriaSinonimoRepository.save(nuevoSinonimo);

    // Limpiar cache para forzar recarga
    cacheNormalizacion.clear();
  }

  public List<Categoria> obtenerTodasLasCategorias() {
    return categoriaRepository.findAll();
  }

  public Optional<Categoria> obtenerCategoriaPorId(Long id) {
    return categoriaRepository.findById(id);
  }

  public List<CategoriaSinonimo> obtenerSinonimosPorCategoria(Long categoriaId) {
    Categoria categoria = categoriaRepository
        .findById(categoriaId)
        .orElseThrow(() -> new CategoriaNoEncontradaException(
            "Categoría con ID " + categoriaId + " no encontrada"));

    return categoriaSinonimoRepository.findByCategoria(categoria);
  }

  private String limpiarTexto(String texto) {
    if (!StringUtils.hasText(texto)) {
      return texto;
    }

    return texto.trim()
        .replaceAll("\\s+", " ")
        .toLowerCase();
  }

  private void validarSinonimo(String sinonimo) {
    if (!StringUtils.hasText(sinonimo)) {
      throw new SinonimoInvalidoException("El sinónimo no puede estar vacío");
    }

    if (sinonimo.trim().length() < 2) {
      throw new SinonimoInvalidoException("El sinónimo debe tener al menos 2 caracteres");
    }

    if (sinonimo.trim().length() > 255) {
      throw new SinonimoInvalidoException("El sinónimo no puede exceder 255 caracteres");
    }
  }

  public void limpiarCache() {
    cacheNormalizacion.clear();
  }
}
