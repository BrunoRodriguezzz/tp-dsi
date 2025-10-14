package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.ContenidoMultimedia;

public class ContenidoMultimediaDTO {
    public static String convertir(ContenidoMultimedia contenidoMultimedia){
        return contenidoMultimedia.getUrl();
    }
}