package ar.edu.utn.frba.dds.client.dtos;

import java.util.List;

public class PaginaDTO<T> {
    private List<T> content;
    // Puedes agregar más campos de paginación si los necesitas
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
}

