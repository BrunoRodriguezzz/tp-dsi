package ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.ValidationException;
import ar.edu.utn.frba.dds.agregador.models.domain.ER_ValueObjects.CategoriaInvalidaException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    public Categoria(String titulo) throws ValidationException {
        if(titulo == null || titulo.isBlank())
            throw new ValidationException("La Categoria no puede tener un titulo vacío");
        this.titulo = titulo;
    }
}
