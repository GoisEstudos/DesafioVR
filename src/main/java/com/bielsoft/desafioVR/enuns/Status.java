package com.bielsoft.desafioVR.enuns;

public enum Status {
    Ativo,
    Inativo;

    public Boolean isAtivo() {
        return this == Ativo;
    }
    public Boolean isInativo() {
        return this == Inativo;
    }

}
