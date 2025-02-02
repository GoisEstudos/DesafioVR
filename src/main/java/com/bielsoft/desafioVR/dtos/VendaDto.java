package com.bielsoft.desafioVR.dtos;

import com.bielsoft.desafioVR.entities.Venda;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VendaDto(
        UUID codigo,
        @Valid
        ClienteDto cliente,
        @Valid
        List<ItemVendaDto> itens,
        @NotNull(message = "A data da venda deve ser informada.")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataVenda,
        Double valorTotal
) {

    public VendaDto(Venda venda) {
        this(
                venda.getCodigo(),
                new ClienteDto(venda.getCliente()),
                venda.getItens().stream().map(ItemVendaDto::new).toList(),
                venda.getDataVenda(),
                venda.getValorTotal()
        );
    }
}
