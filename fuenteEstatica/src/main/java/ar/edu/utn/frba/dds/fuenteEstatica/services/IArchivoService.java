package ar.edu.utn.frba.dds.fuenteEstatica.services;

import ar.edu.utn.frba.dds.fuenteEstatica.models.entities.Archivo;
import java.util.List;

public interface IArchivoService {
  public void guardarArchivo(Archivo archivo);
  public List<Archivo> obtenerArchivos();
}
