package ar.edu.utn.frba.dds.agregador.models.domain.comparador;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.ubicacion.Ubicacion;

public interface IComparacionUbicacion {
    public boolean comparar(Ubicacion ubicacion1, Ubicacion ubicacion2);
}
