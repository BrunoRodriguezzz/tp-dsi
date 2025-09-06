package ar.edu.utn.frba.dds.agregador.models.domain.fuentes.adapters;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;

public interface IAdapUbicacion {
    public Ubicacion buscarUbicacion(String latitud, String longitud);
}
