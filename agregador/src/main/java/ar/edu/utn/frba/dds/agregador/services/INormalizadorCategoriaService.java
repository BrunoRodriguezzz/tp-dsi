package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.normalizador.CategoriaSinonimo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

public interface INormalizadorCategoriaService {
  public List<String> normalizarCategorias(List<String> categorias);
  public String normalizarCategoria(String categoria);
  public void agregarSinonimo(Long categoriaId, String sinonimo);
  public List<Categoria> obtenerTodasLasCategorias();
  public Optional<Categoria> obtenerCategoriaPorId(Long id);
  public List<CategoriaSinonimo> obtenerSinonimosPorCategoria(Long categoriaId);
  public void limpiarCache();
}
