package com.bielsoft.desafioVR.enuns;

public enum StatusCliente {
    Ativo,
    Inativo;

    public Boolean isAtivo() {
        return this == Ativo;
    }
    public Boolean isInativo() {
        return this == Inativo;
    }

}
