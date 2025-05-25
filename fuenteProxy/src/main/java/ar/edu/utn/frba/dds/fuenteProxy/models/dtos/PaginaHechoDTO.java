package ar.edu.utn.frba.dds.fuenteProxy.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PaginaHechoDTO {
    private List<HechoDTO> data;
    private String next_page_url;
}
