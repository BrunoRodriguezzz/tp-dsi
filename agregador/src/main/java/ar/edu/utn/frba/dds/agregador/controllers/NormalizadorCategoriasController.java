package ar.edu.utn.frba.dds.agregador.controllers;


import ar.edu.utn.frba.dds.agregador.models.domain.normalizador.CategoriaSinonimo;
import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.CategoriaSinonimoInputDTO;
import ar.edu.utn.frba.dds.agregador.services.INormalizadorCategoriaService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
public class NormalizadorCategoriasController {
  @Autowired
  private INormalizadorCategoriaService normalizadorService;

  @PostMapping("/normalizar")
  public ResponseEntity<List<String>> normalizarCategorias(@RequestBody List<String> categorias) {
    List<String> categoriasNormalizadas = normalizadorService.normalizarCategorias(categorias);
    return ResponseEntity.ok(categoriasNormalizadas);
  }

  @PostMapping("/sinonimo")
  public ResponseEntity<Map<String, String>> agregarSinonimo(
      @RequestBody CategoriaSinonimoInputDTO request) {

    normalizadorService.agregarSinonimo(
        request.getCategoriaId(),
        request.getSinonimo()
    );

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of("mensaje", "Sinónimo agregado exitosamente"));
  }

  @GetMapping
  public ResponseEntity<List<Categoria>> obtenerCategorias() {
    List<Categoria> categorias = normalizadorService.obtenerTodasLasCategorias();
    return ResponseEntity.ok(categorias);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
    Optional<Categoria> categoria = normalizadorService.obtenerCategoriaPorId(id);
    return categoria.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}/sinonimos")
  public ResponseEntity<List<CategoriaSinonimo>> obtenerSinonimosPorCategoria(@PathVariable Long id) {
    List<CategoriaSinonimo> sinonimos = normalizadorService.obtenerSinonimosPorCategoria(id);
    return ResponseEntity.ok(sinonimos);
  }

  @PostMapping("/cache/limpiar")
  public ResponseEntity<Map<String, String>> limpiarCache() {
    normalizadorService.limpiarCache();
    return ResponseEntity.ok(Map.of("mensaje", "Cache limpiado exitosamente"));
  }
}
