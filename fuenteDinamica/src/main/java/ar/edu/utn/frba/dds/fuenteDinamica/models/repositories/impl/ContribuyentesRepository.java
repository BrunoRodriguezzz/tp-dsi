package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IContribuyentesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContribuyentesRepository implements IContribuyentesRepository {

    private List<Contribuyente> contribuyentes;

    public ContribuyentesRepository(){
        this.contribuyentes = new ArrayList<>();
    }

    @Override
    public void guardar(Contribuyente contribuyente) {

        if(!(comprobarUsuarioRegistrado(contribuyente))){

            contribuyente.setId((long) this.contribuyentes.size());
            this.contribuyentes.add(contribuyente);

        } else{

            //Ya esta registrado.

        }
    }

    @Override
    public Boolean comprobarUsuarioRegistrado(Contribuyente contribuyente){

        return this.contribuyentes
                .stream()
                .anyMatch(usuario -> usuario.getNombre() == contribuyente.getNombre());

    }
}