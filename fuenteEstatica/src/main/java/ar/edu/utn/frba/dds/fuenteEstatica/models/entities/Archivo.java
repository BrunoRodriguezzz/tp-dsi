package ar.edu.utn.frba.dds.fuenteEstatica.models.entities;

import ar.edu.utn.frba.dds.domain.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.models.entities.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Setter
@Getter
public class Archivo {
    private Long id;
    private String nombre;
    private String rutaArchivo;
    // TODO: me llevo la fuenteEstatica del Dominio?? porque esa interfaz Fuente en realidad sería el tipo de Archivo de Fuente Estática
    private Fuente tipoArchivo;

    public List<Hecho> importarHechos(){
        return tipoArchivo.importarHechos();
    }
}
