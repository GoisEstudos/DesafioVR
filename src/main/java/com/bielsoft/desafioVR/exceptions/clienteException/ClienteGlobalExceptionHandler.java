package com.bielsoft.desafioVR.exceptions.clienteException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClienteGlobalExceptionHandler {

@ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(ClienteNotFoundException ex) {
        return ResponseEntity.status(404).body(new ExceptionDto(ex.getMessage()));
    }
}
