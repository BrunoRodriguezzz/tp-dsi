package ar.edu.utn.frba.dds.agregador.controllers;

import ar.edu.utn.frba.dds.agregador.models.dtos.output.ErrorOutputDTO;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
  @ExceptionHandler(value = RuntimeException.class)
  //ResponseEntity es una clase envoltorio que representa toda una respuesta HTTP
  public ResponseEntity<ErrorOutputDTO> handleRuntimeException(RuntimeException exception){
    ErrorOutputDTO error = ErrorOutputDTO.builder()
        .message(exception.getMessage())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
