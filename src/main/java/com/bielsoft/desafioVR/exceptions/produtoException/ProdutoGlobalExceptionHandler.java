package com.bielsoft.desafioVR.exceptions.produtoException;

import com.bielsoft.desafioVR.exceptions.ExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProdutoGlobalExceptionHandler {

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(ProdutoNotFoundException ex) {
        return ResponseEntity.status(404).body(new ExceptionDto(ex.getMessage()));
    }
}
