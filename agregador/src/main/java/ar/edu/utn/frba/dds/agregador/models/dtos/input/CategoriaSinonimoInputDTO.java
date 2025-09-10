package ar.edu.utn.frba.dds.agregador.models.dtos.input;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class CategoriaSinonimoInputDTO {
  private Long categoriaId;
  private String sinonimo;

  public CategoriaSinonimoInputDTO() {}

  public CategoriaSinonimoInputDTO(Long categoriaId, String sinonimo) {
    this.categoriaId = categoriaId;
    this.sinonimo = sinonimo;
  }
}
