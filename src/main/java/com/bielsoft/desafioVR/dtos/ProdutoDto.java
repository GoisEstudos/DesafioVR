package com.bielsoft.desafioVR.dtos;

import com.bielsoft.desafioVR.entities.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProdutoDto(
        UUID codigo,
        @NotBlank(message = "A descricao do produto deve ser informada.")
        String descricao,
        @NotNull(message = "O preco do produto deve ser informado.")
        Double preco
) {

    public ProdutoDto(Produto produto) {
        this(
                produto.getCodigo(),
                produto.getDescricao(),
                produto.getPreco()
        );
    }
}
