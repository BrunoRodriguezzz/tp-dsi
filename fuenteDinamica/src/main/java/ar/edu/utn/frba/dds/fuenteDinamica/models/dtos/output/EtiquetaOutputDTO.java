package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Etiqueta;

public class EtiquetaOutputDTO {
    public static String convertir(Etiqueta etiqueta){
        return etiqueta.getTitulo();
    }
}