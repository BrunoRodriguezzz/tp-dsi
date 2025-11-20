package ar.edu.utn.frba.dds.client.dtos;

import ar.edu.utn.frba.dds.client.dtos.hecho.HechoFormDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoInputDTO {
    private String        nombreUsuario;
    private String        apellidoUsuario;
    private LocalDate     fechaNacimientoUsuario;
    private String        titulo;
    private String        descripcion;
    private String        categoria;
    private List<String>  contenidoMultimedia;
    private String        latitud;
    private String        longitud;
    private String        provincia;
    private String        municipio;
    private LocalDateTime fechaAcontecimiento;
    private Long          idUsuario;

    public static  HechoInputDTO fromHechoFormDTO(HechoFormDTO hechoFormDTO, List<String> pathMultimedia) {
        return HechoInputDTO.builder()
                .nombreUsuario(hechoFormDTO.getNombreUsuario())
                .apellidoUsuario(hechoFormDTO.getApellidoUsuario())
                .fechaNacimientoUsuario(hechoFormDTO.getFechaNacimientoUsuario())
                .titulo(hechoFormDTO.getTitulo())
                .descripcion(hechoFormDTO.getDescripcion())
                .categoria(hechoFormDTO.getCategoria())
                .contenidoMultimedia(pathMultimedia)
                .latitud(hechoFormDTO.getLatitud())
                .longitud(hechoFormDTO.getLongitud())
                .provincia(hechoFormDTO.getProvincia())
                .municipio(hechoFormDTO.getMunicipio())
                .fechaAcontecimiento(hechoFormDTO.getFechaAcontecimiento())
                .idUsuario(hechoFormDTO.getIdUsuario())
                .build();
    }
}
