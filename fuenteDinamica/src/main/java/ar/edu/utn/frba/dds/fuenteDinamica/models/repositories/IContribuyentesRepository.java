package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;

public interface IContribuyentesRepository {
    public void    guardar (Contribuyente contribuyente);
    public Boolean comprobarUsuarioRegistrado(Contribuyente contribuyente);
}
