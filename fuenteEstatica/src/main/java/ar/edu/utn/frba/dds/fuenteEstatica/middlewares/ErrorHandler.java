package ar.edu.utn.frba.dds.fuenteEstatica.middlewares;

import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.ConflictError;
import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.EmptyError;
import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.NotFoundError;
import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.ValidationError;
import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.dto.ConflictErrorDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.dto.EmptyErrorDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.dto.NotFoundErrorDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.exceptions.dto.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = ConflictError.class)
    public ResponseEntity<ConflictErrorDTO> conflictError(ConflictError exception){
        ConflictErrorDTO error = ConflictErrorDTO.builder()
                .message(exception.getMessage())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NotFoundError.class)
    public ResponseEntity<NotFoundErrorDTO> notFoundError(NotFoundError exception){
        NotFoundErrorDTO error = NotFoundErrorDTO.builder()
                .message(exception.getMessage())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ValidationError.class)
    public ResponseEntity<ValidationErrorDTO> notFoundError(ValidationError exception){
        ValidationErrorDTO error = ValidationErrorDTO.builder()
                .message(exception.getMessage())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmptyError.class)
    public ResponseEntity<EmptyErrorDTO> emptyError(EmptyError exception){
        EmptyErrorDTO error = EmptyErrorDTO.builder()
                .message(exception.getMessage())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
