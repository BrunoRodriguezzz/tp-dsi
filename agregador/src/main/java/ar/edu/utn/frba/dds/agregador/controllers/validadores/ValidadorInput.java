package ar.edu.utn.frba.dds.agregador.controllers.validadores;

import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.RequestException;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.GestionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.agregador.models.dtos.input.UbicacionInputDTO;

public class ValidadorInput {
  public static void validarHechoInputDTO(HechoInputDTO hechoInputDTO) {
    if (hechoInputDTO == null) {
      throw new RequestException("Hecho faltante");
    };

    if (hechoInputDTO.getId() == null) {
      throw new RequestException("id faltante");
    };
    if (hechoInputDTO.getTitulo() == null || hechoInputDTO.getTitulo().isBlank()) {
      throw new RequestException("Titulo faltante");
    };
    if (hechoInputDTO.getDescripcion() == null || hechoInputDTO.getDescripcion().isBlank()) {
      throw new RequestException("Decripcion faltante");
    };
    if (hechoInputDTO.getCategoria() == null || hechoInputDTO.getCategoria().isBlank()) {
      throw new RequestException("Categoria faltante");
    };
    ValidadorInput.validarUbicacionInputDTO(hechoInputDTO.getUbicacion());
    if (hechoInputDTO.getFechaAcontecimiento() == null) {
      throw new RequestException("Fecha de Acontecimiento faltante");
    };
    if (hechoInputDTO.getOrigen() == null || hechoInputDTO.getOrigen().isBlank()) {
      throw new RequestException("Origen faltante");
    };
    if (hechoInputDTO.getFuente() == null || hechoInputDTO.getFuente().isBlank()) {
      throw new RequestException("Fuente faltante");
    };
  }

  public static void validarUbicacionInputDTO(UbicacionInputDTO ubicacionInputDTO) {
    if (ubicacionInputDTO == null) {
      throw new RequestException("Ubicacion faltante");
    };
    if (ubicacionInputDTO.getLatitud() == null) {
      throw new RequestException("Latitud faltante");
    };
    if (ubicacionInputDTO.getLongitud() == null) {
      throw new RequestException("Longitud faltante");
    };
  }

  public static void validarSolicitudInputDTO(SolicitudEliminacionInputDTO solicitudEliminacion) {
    if (solicitudEliminacion == null) {
      throw new RequestException("Solicitud de eliminación faltante");
    }

    if (solicitudEliminacion.getIdHecho() == null) {
      throw new RequestException("ID del hecho faltante");
    }

    if (solicitudEliminacion.getFundamento() == null || solicitudEliminacion.getFundamento().isBlank()) {
      throw new RequestException("Fundamento faltante");
    }

    if (solicitudEliminacion.getFechaCreacion() == null) {
      throw new RequestException("Fecha de creación faltante");
    }

    if (solicitudEliminacion.getIdContribuyente() == null) {
      throw new RequestException("ID del contribuyente faltante");
    }

    if (solicitudEliminacion.getFundamento().length() <= 500) {
      throw new RequestException("El fundamento debe tener más de 500 caracteres, actualmente tiene: " + solicitudEliminacion.getFundamento().length());
    }
  }

  public static void validarGestionInputDTO(GestionInputDTO gestionInputDTO) {
    if (gestionInputDTO == null) {
      throw new RequestException("Gestión faltante");
    }

    if (gestionInputDTO.getIdAdministrador() == null) {
      throw new RequestException("ID del administrador faltante");
    }
  }

}
