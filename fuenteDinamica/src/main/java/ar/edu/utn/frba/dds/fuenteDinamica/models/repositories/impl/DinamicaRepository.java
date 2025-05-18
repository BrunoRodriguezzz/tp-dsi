package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.impl;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DinamicaRepository implements IDinamicaRepository {

    private List<Hecho> hechos;

    public DinamicaRepository(){
        this.hechos = new ArrayList<>();
    }

    @Override
    public Hecho buscarPorID(Long id) {
        return this.hechos
                .stream()
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void guardar(Hecho hecho) {
        if(hecho.getId() == null) {

            hecho.setId((long) this.hechos.size());
            hecho.setFechaGuardado(LocalDate.now());
            hecho.setEstadoHecho(EstadoHecho.PENDIENTE);
            hecho.setEtiquetas(null);
            hecho.setSugerenciaDeCambio(null);
            hecho.setEnviado(false);

            this.hechos.add(hecho);

        } else{

            int indice = hechos.indexOf(hecho);
            this.hechos.set(indice,hecho);
        }
    }

    @Override
    public void eliminar(Hecho hecho) {
        // TODO: Revisar enunciado, ¿puede eliminarse por solicitud o decision del Administrador?
    }

    @Override
    public List<Hecho> mostrarTodos() {
        return this.hechos;
    }

    @Override
    public List<Hecho> mostrarEnviados(Boolean enviado) {
        List<Hecho> hechosSolicitados = this.hechos
                .stream()
                .filter(hecho -> hecho.getEnviado() == enviado)
                .toList();

        if(!enviado) {
            for (Hecho hecho : hechosSolicitados) {
                hecho.setEnviado(true);
                this.guardar(hecho);
            }
        }
        return hechosSolicitados;
    }
}