package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Administrador;
import lombok.Data;

@Data
public class AdministradorInputDTO {
  private String nombre;
  private String apellido;

  public static Administrador DTOtoAdministrador (AdministradorInputDTO administradorDTO){
    Administrador administrador = new Administrador(
        administradorDTO.getNombre(),
        administradorDTO.getApellido()
    );
    return administrador;
  }
}
