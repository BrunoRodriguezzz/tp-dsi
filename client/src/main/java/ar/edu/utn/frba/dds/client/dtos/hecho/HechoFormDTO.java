package ar.edu.utn.frba.dds.client.dtos.hecho;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechoFormDTO {
    private String nombreUsuario;
    private String apellidoUsuario;
    private LocalDate fechaNacimientoUsuario;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<MultipartFile> contenidoMultimedia;
    private String latitud;
    private String longitud;
    private String provincia;
    private String municipio;
    private LocalDateTime fechaAcontecimiento;
    private Long idUsuario;
}
