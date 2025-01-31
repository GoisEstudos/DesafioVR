package com.bielsoft.desafioVR.entities;

import com.bielsoft.desafioVR.enuns.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID codigo;
    private String descricao;
    private Double preco;
    @Enumerated(EnumType.STRING)
    private Status status = Status.Ativo;

}
