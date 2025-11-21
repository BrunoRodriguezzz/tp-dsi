package ar.edu.utn.frba.dds.client.services;

import ar.edu.utn.frba.dds.client.dtos.SolicitudEliminacionConHechoDTO;
import ar.edu.utn.frba.dds.client.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudesService {
  private final WebApiCallerService webApiCallerService;
  private final String backendBaseUrl;

  public SolicitudesService(WebApiCallerService webApiCallerService, @Value("${servicio.agregador}") String backendBaseUrl) {
    this.webApiCallerService = webApiCallerService;
    this.backendBaseUrl = backendBaseUrl;
  }

  public List<SolicitudEliminacionConHechoDTO> obtenerSolicitudesEliminacion() {
    String url = backendBaseUrl +  "/solicitudesEliminacion";
    return webApiCallerService.getList(url, SolicitudEliminacionConHechoDTO.class);
  }

  public void aceptarSolicitud(Long id, String adminNombre, String adminApellido) {
    String url = backendBaseUrl + "/solicitudesEliminacion/aceptacion/" + id;
    AdminDTO body = new AdminDTO(adminNombre, adminApellido);
    webApiCallerService.patch(url, body, Void.class);
  }

  public void rechazarSolicitud(Long id, String adminNombre, String adminApellido) {
    String url = backendBaseUrl + "/solicitudesEliminacion/rechazo/" + id;
    AdminDTO body = new AdminDTO(adminNombre, adminApellido);
    webApiCallerService.patch(url, body, Void.class);
  }

  public static class AdminDTO {
    private String administradorNombre;
    private String administradorApellido;

    public AdminDTO(String administradorNombre, String administradorApellido) {
      this.administradorNombre = administradorNombre;
      this.administradorApellido = administradorApellido;
    }

    public String getAdministradorNombre() { return administradorNombre; }
    public void setAdministradorNombre(String administradorNombre) { this.administradorNombre = administradorNombre; }
    public String getAdministradorApellido() { return administradorApellido; }
    public void setAdministradorApellido(String administradorApellido) { this.administradorApellido = administradorApellido; }
  }
}
