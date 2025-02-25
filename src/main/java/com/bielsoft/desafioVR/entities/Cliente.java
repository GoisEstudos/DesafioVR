package com.bielsoft.desafioVR.entities;

import com.bielsoft.desafioVR.enuns.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID codigo;
    private String nome;
    private Double limiteCredito;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechamentoFatura;
    @Enumerated(EnumType.STRING)
    private Status status = Status.Ativo;

}
