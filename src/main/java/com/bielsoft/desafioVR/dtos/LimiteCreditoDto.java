package com.bielsoft.desafioVR.dtos;

import java.time.LocalDate;
import java.util.List;

public record LimiteCreditoDto(
        Double limiteCredito,
        Double totalComprasAposFechamento,
        Double valorDisponivel,
        boolean limiteExcedido,
        LocalDate proximoFechamento,
        List<VendaDto> comprasAposFechamento
) {
    // Construtor adicional para casos onde não há compras após o fechamento
    public LimiteCreditoDto(Double limiteCredito, Double totalComprasAposFechamento, Double valorDisponivel, boolean limiteExcedido, LocalDate proximoFechamento) {
        this(limiteCredito, totalComprasAposFechamento, valorDisponivel, limiteExcedido, proximoFechamento, null);
    }
}