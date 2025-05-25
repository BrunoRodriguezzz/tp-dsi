package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoNoAutorizado;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorAccesoProhibido;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorDeTiempo;
import ar.edu.utn.frba.dds.fuenteDinamica.excepciones.ErrorTipoDeDatos;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = ErrorAccesoProhibido.class)
    public ResponseEntity<ErrorDTO> errorAccesoProhibido(RuntimeException excepcion){
        ErrorDTO error = ErrorDTO
                .builder()
                .mensaje(excepcion.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ErrorTipoDeDatos.class)
    public ResponseEntity<ErrorDTO> errorTipoDeDatos(RuntimeException excepcion){
        ErrorDTO error = ErrorDTO
                .builder()
                .mensaje(excepcion.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ErrorAccesoNoAutorizado.class)
    public ResponseEntity<ErrorDTO> errorUsuarioNoRegistrado(RuntimeException excepcion){
        ErrorDTO error = ErrorDTO
                .builder()
                .mensaje(excepcion.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ErrorDeTiempo.class)
    public ResponseEntity<ErrorDTO> errorTiempoParaModificar(RuntimeException excepcion){
        ErrorDTO error = ErrorDTO
                .builder()
                .mensaje(excepcion.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.REQUEST_TIMEOUT);
    }
}
