package ar.edu.utn.frba.dds.fuenteDinamica.services.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repositories.IDinamicaRepository;
import ar.edu.utn.frba.dds.fuenteDinamica.services.IDinamicaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DinamicaService implements IDinamicaService {

    private IDinamicaRepository dinamicaRepository;

    @Override
    public List<HechoOutputDTO> buscarHechos(Boolean enviado) {
        if(enviado != null){
            return dinamicaRepository
                    .mostrarEnviados(enviado)
                    .stream()
                    .map(this::hechoOutputDTO)
                    .toList();
        }

        return this.dinamicaRepository
                .mostrarTodos()
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    @Override
    public HechoOutputDTO buscarPorID(Long id) {
        return null;
    }

    @Override
    public HechoOutputDTO crear(HechoInputDTO hechoInputDTO) {

        Hecho hecho = new Hecho();

        hecho.setTitulo(hechoInputDTO.getTitulo());
        hecho.setDescripcion(hechoInputDTO.getDescripcion());
        hecho.setCategoria(hechoInputDTO.getCategoria());
        hecho.setUrlMultimedia(hechoInputDTO.getUrlMultimedia());
        hecho.setUbicacion((hechoInputDTO.getUbicacion()));
        hecho.setFechaAcontecimiento(hechoInputDTO.getFechaAcontecimiento());

        Contribuyente usuario = new Contribuyente();

        usuario.setNombre(hechoInputDTO.getNombreUsuario());
        usuario.setEdad(hechoInputDTO.getEdadUsuario());

        hecho.setContribuyente(usuario);

        this.dinamicaRepository.guardar(hecho);

        return this.hechoOutputDTO(hecho);
    }

    @Override
    public void eliminar(Long id) {
        // TODO: Revisar enunciado, ¿puede eliminarse por solicitud o decision del Administrador?
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho){

        HechoOutputDTO hechoOutputDTO = new HechoOutputDTO();

        hechoOutputDTO.setContribuyente(hecho.getContribuyente());
        hechoOutputDTO.setTitulo(hecho.getTitulo());
        hechoOutputDTO.setDescripcion(hecho.getDescripcion());
        hechoOutputDTO.setCategoria(hecho.getCategoria());
        hechoOutputDTO.setUrlMultimedia(hecho.getUrlMultimedia());
        hechoOutputDTO.setUbicacion(hecho.getUbicacion());
        hechoOutputDTO.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        hechoOutputDTO.setEtiquetas(hecho.getEtiquetas());

        return hechoOutputDTO;
    }
}