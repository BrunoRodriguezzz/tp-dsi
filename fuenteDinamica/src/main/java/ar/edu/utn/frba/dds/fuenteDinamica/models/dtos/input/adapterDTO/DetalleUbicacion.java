package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.adapterDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetalleUbicacion {
    private ProvinciaDTO provincia;
    private MunicipioDTO municipio;
}
