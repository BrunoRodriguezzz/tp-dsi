package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import lombok.Data;

@Data
public class AdministradorOutputDTO {
  private String nombre;
  private String apellido;

  public static AdministradorOutputDTO AdministradorToDTO (Administrador administrador){
    AdministradorOutputDTO administradorDTO = new AdministradorOutputDTO();
    administradorDTO.setNombre(administrador.getNombre());
    administradorDTO.setApellido(administrador.getApellido());
    return administradorDTO;
  }
}
