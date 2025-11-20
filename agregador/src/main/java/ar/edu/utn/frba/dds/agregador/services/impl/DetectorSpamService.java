package ar.edu.utn.frba.dds.agregador.services.impl;

import ar.edu.utn.frba.dds.agregador.services.IDetectorSpamService;
import ar.edu.utn.frba.dds.agregador.models.domain.solicitudEliminacion.SolicitudEliminacion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DetectorSpamService implements IDetectorSpamService {
  // Fundamentos analizados previamente
  private final List<String> corpus;

  public DetectorSpamService() {
    this.corpus = new ArrayList<>();
  }

  // Palabras que debería tener un fundamento
  private final Set<String> palabrasClaveValidas = Set.of(
      "erróneo", "equivocado", "ofensivo", "inexacto", "privacidad",
      "difamación", "acoso", "incorrecto", "mentira", "error", "eliminación"
  );
//  erróneo equivocado ofensivo inexacto privacidad difamación acoso incorrecto
  public boolean esFundamentoValido(String fundamento) {
    // TF-IDF: importancia de una palabra respecto al resto del corpus
    Map<String, Double> tfidf = calcularTFIDF(fundamento, corpus);

    // Score: suma el peso TF-IDF de palabras clave válidas
    double score = tfidf.entrySet().stream()
        .filter(entry -> palabrasClaveValidas.contains(entry.getKey().toLowerCase()))
        .mapToDouble(Map.Entry::getValue)
        .sum();
    // Se fija cuánto pesan las palabras clave válidas en ese texto. Si hay muchas, el texto probablemente sea válido

    // Cheackear repeticion de letras
    if (fundamento.matches(".*(.)\\1{4,}.*")) {
      return false;
    }
    Boolean resultado = score + 0.03 > 0.02;
    return resultado; // Umbral ajustable
  }

  private Map<String, Double> calcularTFIDF(String texto, List<String> documentos) {
    // Dividir texto en palabras
    List<String> palabras = dividirEnPalabras(texto);

    // Contar cuántas veces aparece cada palabra
    Map<String, Long> tf = palabras.stream()
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

    Map<String, Double> tfidf = new HashMap<>();
    int totalDocs = documentos.size();
    if(totalDocs == 0) {
      totalDocs = 1;
    }

    // Para cada palabra, calcula su TF * IDF
    for (String palabra : tf.keySet()) {
      double frecuencia = (double) tf.get(palabra) / palabras.size();
      long docsConPalabra = documentos.stream()
          .filter(doc -> dividirEnPalabras(doc).contains(palabra))
          .count();
      double idf = Math.log((double) (totalDocs + 1) / (docsConPalabra + 1));
      tfidf.put(palabra, frecuencia * idf);
    }

    // Los resultados se guardan en el mapa
    return tfidf;
  }

  private List<String> dividirEnPalabras(String texto) {
    return Arrays.stream(texto.toLowerCase().split("\\W+"))
        .filter(s -> !s.isBlank())
        .collect(Collectors.toList());
  }


  public Boolean esSpam(SolicitudEliminacion solicitud) {
    Boolean resultado = this.esFundamentoValido(solicitud.getFundamento());
    if(resultado) {
      return false;
    }
    corpus.add(solicitud.getFundamento());
    return true;
  }
}
