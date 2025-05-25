package ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.impl;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class DinamicaRepository implements IDinamicaRepository {

    private List<Hecho> hechos;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public DinamicaRepository(){
        this.hechos = new ArrayList<>();
    }

    @Override
    public Hecho buscarPorID(Long id) {
        return this.hechos
                .stream()
                .filter(hecho -> hecho.getIdHecho().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void guardar(Hecho hecho) {

        Long id = idGenerator.getAndIncrement();
        hecho.setIdHecho(id);
        hecho.setFechaGuardado(LocalDateTime.now());
        hecho.setEstadoHecho(EstadoHecho.PENDIENTE_DE_REVISION);
        hecho.setEtiquetas(null);
        hecho.setSugerenciaDeCambio(null);
        hecho.setEnviado(false);

        this.hechos.add(hecho);
    }

    @Override
    public void guardarCambios(Hecho hechoOriginal, Hecho hechoCambiado){

        int indice = this.hechos.indexOf(hechoOriginal);
        this.hechos.set(indice,hechoCambiado);

    }

    @Override
    public void eliminar(Hecho hecho) {
        // TODO: Revisar enunciado, ¿puede eliminarse por solicitud o decision del Administrador?
    }

    @Override
    public List<Hecho> mostrarTodos() {
        return this.hechos
                .stream()
                .filter(hecho -> estadoAceptado(hecho))
                .toList();
    }

    @Override
    public List<Hecho> mostrarEnviados(Boolean enviado) {
        List<Hecho> hechosSolicitados = this.mostrarTodos()
                .stream()
                .filter(hecho -> hecho.getEnviado() == enviado)
                .toList();

        if(!enviado) {
            for (Hecho hecho : hechosSolicitados) {

                Hecho hechoCambiado = this.buscarPorID(hecho.getIdHecho());

                hechoCambiado.setEnviado(true);

                this.guardarCambios(hecho, hechoCambiado);
            }
        }
        return hechosSolicitados;
    }

    private Boolean estadoAceptado(Hecho hecho){

        return (hecho.getEstadoHecho() == EstadoHecho.ACEPTADA) || (hecho.getEstadoHecho() == EstadoHecho.ACEPTADA_CON_SUGERENCIA);

    }
}