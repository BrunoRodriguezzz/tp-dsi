package ar.edu.utn.frba.dds.servicioAutenticacion.domain.models;

import ar.edu.utn.frba.dds.servicioAutenticacion.converter.PermisoConverter;
import ar.edu.utn.frba.dds.servicioAutenticacion.converter.RolConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(nullable = false, name = "nombre")
    @NotNull(message = "Se debe ingresar un nombre")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(nullable = false, name = "apellido")
    @NotNull(message = "Se debe ingresar un apellido")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @Column(nullable = false, name = "fechaNacimiento")
    @NotNull(message = "Se debe ingresar una fecha de nacimiento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @Column(nullable = false, name = "username")
    @NotNull(message = "Se debe ingresar un nombre de usuario")
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @Column(nullable = false, name = "password")
    @NotNull(message = "La contraseña del usuario no puede ser nula")
    @NotBlank(message = "La contraseña del usuario no puede estar vacía")
    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    )
    @Column(name = "roles")
    @Convert(converter = RolConverter.class)
    @NotNull(message = "Los roles no pueden ser vacios")
    private List<Rol> roles;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "usuario_permisos",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    )
    @Column(name = "permisos")
    @Convert(converter = PermisoConverter.class)
    @NotNull(message = "Los permisos no pueden ser vacios")
    private List<Permiso> permisos;
}
