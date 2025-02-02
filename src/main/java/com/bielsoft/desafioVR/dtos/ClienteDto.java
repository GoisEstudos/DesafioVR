package com.bielsoft.desafioVR.dtos;

import com.bielsoft.desafioVR.entities.Cliente;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ClienteDto(
        UUID codigo,
        @NotBlank(message = "O nome do cliente deve ser informado.")
        String nome,
        @NotNull(message = "O limite de crédito deve ser informado.")
        Double limiteCredito,
        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull(message = "A data de fechamento da fatura deve ser informada.")
        @FutureOrPresent(message = "A data não pode ser no passado.")
        LocalDate fechamentoFatura) {

    public ClienteDto(Cliente cliente) {
        this(
                cliente.getCodigo(),
                cliente.getNome(),
                cliente.getLimiteCredito(),
                cliente.getFechamentoFatura()
        );
    }
}
