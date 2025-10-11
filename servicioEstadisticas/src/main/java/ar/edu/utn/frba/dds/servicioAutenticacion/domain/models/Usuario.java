package ar.edu.utn.frba.dds.servicioAutenticacion.domain.models;

import ar.edu.utn.frba.dds.servicioAutenticacion.converter.RolConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "username")
    private String username;
    @Column(nullable = false, name = "password")
    @JsonIgnore
    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    )
    @Column(name = "roles")
    @Convert(converter = RolConverter.class)
    private List<Rol> roles;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "usuario_permisos",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    )
    @Column(name = "permisos")
    @Convert(converter = RolConverter.class)
    private List<Permiso> permisos;
}
