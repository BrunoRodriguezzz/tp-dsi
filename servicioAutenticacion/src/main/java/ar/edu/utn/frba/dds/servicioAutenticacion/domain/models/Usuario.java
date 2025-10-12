package ar.edu.utn.frba.dds.servicioAutenticacion.domain.models;

import ar.edu.utn.frba.dds.servicioAutenticacion.converter.PermisoConverter;
import ar.edu.utn.frba.dds.servicioAutenticacion.converter.RolConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Column(nullable = false, name = "username")
    @NotNull(message = "El nombre del usuario no puede ser nula")
    @NotBlank(message = "El nombre del usuario no puede estar vacio")
    private String username;

    @Column(nullable = false, name = "password")
    @NotNull(message = "La contraseña del usuario no puede ser nula")
    @NotBlank(message = "La contraseña del usuario no puede estar vacia")
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
