package com.bielsoft.desafioVR.dtos;

import com.bielsoft.desafioVR.entities.ItemVenda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ItemVendaDto(
        UUID codigo,
        @Valid
        ProdutoDto produto,
        @NotNull(message = "A quantidade do item da venda deve ser informada.")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {

    public ItemVendaDto(ItemVenda itemVenda) {
        this(
                itemVenda.getCodigo(),
                new ProdutoDto(itemVenda.getProduto()),
                itemVenda.getQuantidade()
        );
    }
}
