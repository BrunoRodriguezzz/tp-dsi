package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IContribuyenteRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ContribuyenteRepository implements IContribuyenteRepository {

    private List<Contribuyente> contribuyentes;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ContribuyenteRepository(){

        this.contribuyentes = new ArrayList<>();

    }

    @Override
    public void guardar(Contribuyente contribuyente) {

        if(!(this.comprobarUsuarioRegistrado(contribuyente))){
            Long id = idGenerator.getAndIncrement();
            contribuyente.setIdContribuyente(id);
            this.contribuyentes.add(contribuyente);

        } else{

            Contribuyente cuentaDelUsuario = this.contribuyentes
                    .stream()
                    .filter(usuario ->
                            usuario.getNombre().equals(contribuyente.getNombre())
                            && usuario.getApellido().equals(contribuyente.getApellido())
                            && usuario.getFechaNacimiento().equals(contribuyente.getFechaNacimiento()))
                    .findFirst().orElse(null);

            contribuyente.setIdContribuyente(cuentaDelUsuario.getIdContribuyente());
        }
    }

    @Override
    public Boolean comprobarUsuarioRegistrado(Contribuyente contribuyente){

        return this.contribuyentes
                .stream()
                .anyMatch(usuario ->
                        usuario.getNombre().equals(contribuyente.getNombre())
                        && usuario.getApellido().equals(contribuyente.getApellido())
                        && usuario.getFechaNacimiento().equals(contribuyente.getFechaNacimiento()));

    }
}