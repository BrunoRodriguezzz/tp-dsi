package ar.edu.utn.frba.dds.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {
    private String provincia;
    private String municipio;
    private String latitud;
    private String longitud;

  public String nombreProvincia() {
    if (this.provincia == null) {
      return null;
    }

    return switch (this.provincia) {
      case "BUENOS_AIRES" -> "Buenos Aires";
      case "CABA" -> "Ciudad Autónoma de Buenos Aires";
      case "CATAMARCA" -> "Catamarca";
      case "CHACO" -> "Chaco";
      case "CHUBUT" -> "Chubut";
      case "CORDOBA" -> "Córdoba";
      case "CORRIENTES" -> "Corrientes";
      case "ENTRE_RIOS" -> "Entre Ríos";
      case "FORMOSA" -> "Formosa";
      case "JUJUY" -> "Jujuy";
      case "LA_PAMPA" -> "La Pampa";
      case "LA_RIOJA" -> "La Rioja";
      case "MENDOZA" -> "Mendoza";
      case "MISIONES" -> "Misiones";
      case "NEUQUEN" -> "Neuquén";
      case "RIO_NEGRO" -> "Río Negro";
      case "SALTA" -> "Salta";
      case "SAN_JUAN" -> "San Juan";
      case "SAN_LUIS" -> "San Luis";
      case "SANTA_CRUZ" -> "Santa Cruz";
      case "SANTA_FE" -> "Santa Fe";
      case "SANTIAGO_DEL_ESTERO" -> "Santiago del Estero";
      case "TIERRA_DEL_FUEGO" -> "Tierra del Fuego";
      case "TUCUMAN" -> "Tucumán";
      default -> this.provincia;
    };
  }

}