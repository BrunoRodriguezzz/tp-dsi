package ar.edu.utn.frba.dds.client.dtos.hecho;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginadoHechoDTO {
    List<HechoDTO> content;
    int totalElements;
    int totalPages;
    int size;
    int number;
    int numberOfElements;
}
