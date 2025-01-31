package com.bielsoft.desafioVR.dtos;

import com.bielsoft.desafioVR.entities.Produto;

import java.util.UUID;

public record ProdutoDto(UUID codigo, String descricao, Double preco) {

    public ProdutoDto(Produto produto) {
        this(
                produto.getCodigo(),
                produto.getDescricao(),
                produto.getPreco()
        );
    }
}
