package ar.edu.utn.frba.dds.servicioAutenticacion.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
