package ar.edu.utn.frba.dds.agregador.exceptions;

import ar.edu.utn.frba.dds.agregador.exceptions.dtos.ExceptionDTO;
import ar.edu.utn.frba.dds.agregador.exceptions.dtos.HechoYaEliminadoExceptionDTO;
import ar.edu.utn.frba.dds.agregador.exceptions.dtos.HechoYaExistenteExceptionDTO;
import ar.edu.utn.frba.dds.agregador.exceptions.dtos.NotFoundExceptionDTO;
import ar.edu.utn.frba.dds.agregador.exceptions.dtos.RequestExceptionDTO;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.HechoYaEliminadoException;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.HechoYaExistenteException;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.agregador.exceptions.exceptions.RequestException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerController {
  @ExceptionHandler(value = RuntimeException.class)
  //ResponseEntity es una clase envoltorio que representa toda una respuesta HTTP
  public ResponseEntity<ExceptionDTO> handleRuntimeException(RuntimeException exception){
    ExceptionDTO error = ExceptionDTO.builder()
        .message(exception.getMessage())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = NotFoundException.class)
  public ResponseEntity<NotFoundExceptionDTO> handleNotFoundException(NotFoundException exception){
    NotFoundExceptionDTO error = NotFoundExceptionDTO.builder()
        .message(exception.getMessage())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = RequestException.class)
  public ResponseEntity<RequestExceptionDTO> handleRequestException(RequestException exception){
    RequestExceptionDTO error = RequestExceptionDTO.builder()
        .message(exception.getMessage())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  // Business
  @ExceptionHandler(value = HechoYaExistenteException.class)
  public ResponseEntity<HechoYaExistenteExceptionDTO> handleHechoYaExistenteException(HechoYaExistenteException exception){
    HechoYaExistenteExceptionDTO error = HechoYaExistenteExceptionDTO.builder()
        .message(exception.getMessage())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .hechoExistente(exception.getHechoExistente())
        .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = HechoYaEliminadoException.class)
  public ResponseEntity<HechoYaEliminadoExceptionDTO> handleHechoYaEliminadoException(HechoYaEliminadoException exception){
    HechoYaEliminadoExceptionDTO error = HechoYaEliminadoExceptionDTO.builder()
        .message(exception.getMessage())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .hechoEliminado(exception.getHechoEliminado())
        .build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}
