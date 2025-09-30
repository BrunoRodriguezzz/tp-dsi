package ar.edu.utn.frba.dds.agregador.services;

import ar.edu.utn.frba.dds.agregador.models.domain.hechos.Hecho;

public interface INormalizadorService {
    public Hecho normalizarHecho(Hecho hecho);
}
