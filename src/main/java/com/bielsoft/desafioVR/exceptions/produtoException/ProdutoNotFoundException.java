package com.bielsoft.desafioVR.exceptions.produtoException;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException(String message) {
        super(message);
    }
}
