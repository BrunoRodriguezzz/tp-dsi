package ar.edu.utn.frba.dds.client.dtos.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;

    @NotNull(message = "Se debe ingresar un nombre")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotNull(message = "Se debe ingresar un apellido")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotNull(message = "Se debe ingresar una fecha de nacimiento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

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
