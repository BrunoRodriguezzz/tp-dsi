package ar.edu.utn.frba.dds.servicioEstadisticas.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginadoInput<T> {
    List<T> content;
    int totalElements;
    int toalPages;
    int size;
    int number;
    int numberOfElements;
}
