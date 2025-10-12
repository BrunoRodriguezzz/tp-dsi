package ar.edu.utn.frba.dds.client.dtos.auth;

import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Permiso;
import ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.Rol;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "El nombre del usuario no puede ser nula")
    @NotBlank(message = "El nombre del usuario no puede estar vacio")
    private String username;

    @NotNull(message = "La contraseña del usuario no puede ser nula")
    @NotBlank(message = "La contraseña del usuario no puede estar vacia")
    private String password;

    @NotNull(message = "Los roles no pueden ser vacios")
    private List<Rol> roles;

    @NotNull(message = "Los permisos no pueden ser vacios")
    private List<Permiso> permisos;
}
