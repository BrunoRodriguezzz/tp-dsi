package ar.edu.utn.frba.dds.agregador.models.dtos.output;

import ar.edu.utn.frba.dds.agregador.models.domain.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.agregador.models.dtos.external.ContribuyenteServicioResponseDTO;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ContribuyenteOutputDTO {
  private String nombre;
  private String apellido;
  private LocalDateTime fechaNacimiento;

  public static ContribuyenteOutputDTO ContribuyenteToDTO(Contribuyente contribuyente) {
    ContribuyenteOutputDTO contribuyenteDTO = new ContribuyenteOutputDTO();
    contribuyenteDTO.setNombre(contribuyente.getNombre());
    contribuyenteDTO.setApellido(contribuyente.getApellido());
    contribuyenteDTO.setFechaNacimiento(contribuyente.getFechaNacimiento());
    return contribuyenteDTO;
  }

  public static Contribuyente contribuyenteServicioResponseDTOtoContribuyente(ContribuyenteServicioResponseDTO contribuyenteDTO) {
    try{
      Contribuyente contribuyente = new Contribuyente(contribuyenteDTO.getNombre(),contribuyenteDTO.getApellido(),contribuyenteDTO.getFechaNacimiento());
      if(contribuyenteDTO.getId() != null){
        contribuyenteDTO.setId(contribuyenteDTO.getId());
      }
      return contribuyente;
    }catch (Exception e){
      throw new RuntimeException(e.getMessage());
    }
  }
}
