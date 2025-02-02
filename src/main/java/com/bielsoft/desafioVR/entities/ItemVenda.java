package com.bielsoft.desafioVR.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "itens_venda")
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID codigo;
    @ManyToOne
    @JoinColumn(name = "produto_codigo")
    private Produto produto;
    @ManyToOne
    @JoinColumn(name = "venda_codigo") // Chave estrangeira para a tabela vendas
    @JsonManagedReference
    private Venda venda;
    private Integer quantidade;
}
