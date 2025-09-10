package ar.edu.utn.frba.dds.agregador.models.domain.normalizador;

import ar.edu.utn.frba.dds.agregador.models.domain.valueObjectsHecho.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "categoria_sinonimo")
public class CategoriaSinonimo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "sinonimo", nullable = false, length = 255)
  private String sinonimo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categoria_id", nullable = false)
  private Categoria categoria;

  public CategoriaSinonimo(String sinonimo, Categoria categoria) {
    this.sinonimo = sinonimo;
    this.categoria = categoria;
  }
}
