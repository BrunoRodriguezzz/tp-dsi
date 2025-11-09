package ar.edu.utn.frba.dds.servicioEstadisticas.controllers.advice;

import ar.edu.utn.frba.dds.servicioEstadisticas.controllers.advice.exceptions.ExceptionDTO;
import ar.edu.utn.frba.dds.servicioEstadisticas.controllers.advice.exceptions.NotFound;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler{
  @ExceptionHandler(value = RuntimeException.class)
  public ResponseEntity<ExceptionDTO> handle(RuntimeException exception){
    ExceptionDTO error = ExceptionDTO.builder()
        .message(exception.getMessage())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = NotFound.class)
  public ResponseEntity<ExceptionDTO> handle(NotFound exception){
    ExceptionDTO error = ExceptionDTO.builder()
        .message(exception.getMessage())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
