package ar.edu.utn.frba.dds.client.dtos;

public class UsuarioLogueadoDTO {
  private String nombre;
  private String apellido;
  private String username;

  public UsuarioLogueadoDTO(String nombre, String apellido, String username) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.username = username;
  }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getApellido() { return apellido; }
  public void setApellido(String apellido) { this.apellido = apellido; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
}