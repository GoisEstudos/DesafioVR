package com.bielsoft.desafioVR.exceptions.vendaException;

import com.bielsoft.desafioVR.exceptions.ExceptionDto;
import com.bielsoft.desafioVR.exceptions.produtoException.ProdutoNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VendaGlobalExceptionHandler {

    @ExceptionHandler(VendaNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleExceptionVendaNotFoundException(ProdutoNotFoundException ex) {
        return ResponseEntity.status(404).body(new ExceptionDto(ex.getMessage()));
    }
    @ExceptionHandler(LimiteCreditoExcedidoException.class)
    public ResponseEntity<ExceptionDto> handleExceptionLimiteCreditoExcedidoException(LimiteCreditoExcedidoException ex) {
        return ResponseEntity.status(404).body(new ExceptionDto(ex.getMessage()));
    }
    @ExceptionHandler(ProdutoRepetidoException.class)
    public ResponseEntity<ExceptionDto> handleExceptionProdutoRepetidoException(ProdutoRepetidoException ex) {
        return ResponseEntity.status(404).body(new ExceptionDto(ex.getMessage()));
    }

}
