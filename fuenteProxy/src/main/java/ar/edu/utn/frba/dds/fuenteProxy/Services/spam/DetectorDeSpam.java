package ar.edu.utn.frba.dds.fuenteProxy.Services.spam;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DetectorDeSpam {

    private static final List<String> PALABRAS_SPAM = List.of(
            "dinero", "click aquí", "estafa"
    );

    public boolean esSpam(String texto) {
        if (texto == null || texto.isBlank()) return true; // si el motivo es vacio => es spam
        String lower = texto.toLowerCase();
        return PALABRAS_SPAM.stream().anyMatch(lower::contains);
    }
}
