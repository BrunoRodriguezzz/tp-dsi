package ar.edu.utn.frba.dds.agregador.schedulers;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class CatNormSchedulerTest {
    private final JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
    private static final double UMBRAL_SIMILARIDAD = 0.86;

    @Test
    void testDetectaSinonimosEntreCategorias() {
        List<String> categorias = List.of("EJEMPLO", "JEM", "OLPMEJE", "EJMPLO", "EJ", "PLO");
        List<String> sinonimosDetectados = new ArrayList<>();
        for (String cat : categorias) {
            for (String otraCat : categorias) {
                if (!cat.equals(otraCat)) {
                    double score = similarity.apply(cat, otraCat);
                    System.out.println(cat + " <-> " + otraCat + " = " + score); // Imprime el score
                    if (score > UMBRAL_SIMILARIDAD) {
                        sinonimosDetectados.add(cat + " <-> " + otraCat);
                    }
                }
            }
        }
    }
}
