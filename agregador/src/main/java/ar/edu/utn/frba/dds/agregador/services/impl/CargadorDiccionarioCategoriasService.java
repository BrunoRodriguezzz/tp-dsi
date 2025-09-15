package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.CategoriaInvalidaException;
import ar.edu.utn.frba.dds.agregador.models.domain.normalizador.CategoriaSinonimo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.agregador.models.repositories.ICategoriaSinonimoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CargadorDiccionarioCategoriasService implements CommandLineRunner {
  @Autowired
  private ICategoriaRepository categoriaRepository;

  @Autowired
  private ICategoriaSinonimoRepository categoriaSinonimoRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void run(String... args) throws Exception {
    if (categoriaRepository.count() == 0) {
      System.out.println("Cargando diccionario de categorías...");
      cargarDiccionario();
      System.out.println("Diccionario cargado exitosamente.");
    } else {
      System.out.println("Ya existen categorías en la base de datos. Omitiendo carga automática.");
    }
  }

  @Transactional
  public void cargarDiccionario() {
    try (InputStream inputStream = new ClassPathResource("utils/diccionario_categorias_actualizado.json").getInputStream()) {
      JsonNode rootNode = objectMapper.readTree(inputStream);
      JsonNode categoriasSimilares = rootNode.get("categorias_similares");

      Iterator<Map.Entry<String, JsonNode>> campos = categoriasSimilares.fields();

      while (campos.hasNext()) {
        Map.Entry<String, JsonNode> entrada = campos.next();
        String nombreGrupo = entrada.getKey();
        JsonNode listaCategorias = entrada.getValue();

        procesarGrupoCategoria(nombreGrupo, listaCategorias);
      }

    } catch (IOException e) {
      throw new RuntimeException("Error al cargar el diccionario de categorías", e);
    }
  }

  private void procesarGrupoCategoria(String nombreGrupo, JsonNode listaCategorias) {
    if (!listaCategorias.isArray() || listaCategorias.size() == 0) {
      return;
    }

    try {
      // La primera categoría del grupo será la principal
      String tituloPrincipal = listaCategorias.get(0).asText();

      Categoria categoriaPrincipal = new Categoria(tituloPrincipal);
      categoriaPrincipal = categoriaRepository.save(categoriaPrincipal);

      List<CategoriaSinonimo> sinonimos = new ArrayList<>();

      // Las demás categorías serán sinónimos
      for (int i = 1; i < listaCategorias.size(); i++) {
        String sinonimo = listaCategorias.get(i).asText();
        CategoriaSinonimo categoriaSinonimo = new CategoriaSinonimo(sinonimo, categoriaPrincipal);
        sinonimos.add(categoriaSinonimo);
      }

      if (!sinonimos.isEmpty()) {
        categoriaSinonimoRepository.saveAll(sinonimos);
      }

      System.out.println("Procesado grupo '" + nombreGrupo + "' con " +
          (listaCategorias.size() - 1) + " sinónimos para '" + tituloPrincipal + "'");

    } catch (CategoriaInvalidaException e) {
      throw new RuntimeException("Error creando categoría para grupo '" + nombreGrupo + "': " + e.getMessage());
    }
  }
}
