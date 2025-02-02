package com.bielsoft.desafioVR.services;

import com.bielsoft.desafioVR.entities.ItemVenda;
import com.bielsoft.desafioVR.entities.Venda;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class VendaSpecification {

    // Filtro por cliente
    public static Specification<Venda> porCliente(UUID clienteId) {
        return (root, query, criteriaBuilder) -> {
            if (clienteId == null) {
                return criteriaBuilder.conjunction(); // Retorna uma condição "verdadeira" (sem filtro)
            }
            return criteriaBuilder.equal(root.get("cliente").get("codigo"), clienteId);
        };
    }

    // Filtro por produto na lista de itens
    public static Specification<Venda> porProduto(UUID produtoId) {
        return (root, query, criteriaBuilder) -> {
            if (produtoId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Venda, ItemVenda> itensJoin = root.join("itens");
            return criteriaBuilder.equal(itensJoin.get("produto").get("codigo"), produtoId);
        };
    }

    // Filtro por data inicial (vendas com dataVenda >= dataInicial)
    public static Specification<Venda> porDataInicial(LocalDateTime dataInicial) {
        return (root, query, criteriaBuilder) -> {
            if (dataInicial == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("dataVenda"), dataInicial);
        };
    }

    // Filtro por data final (vendas com dataVenda <= dataFinal)
    public static Specification<Venda> porDataFinal(LocalDateTime dataFinal) {
        return (root, query, criteriaBuilder) -> {
            if (dataFinal == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("dataVenda"), dataFinal);
        };
    }

    // Filtro por período (dataInicial <= dataVenda <= dataFinal)
    public static Specification<Venda> porPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return (root, query, criteriaBuilder) -> {
            Specification<Venda> spec = Specification.where(null);

            if (dataInicial != null) {
                spec = spec.and(porDataInicial(dataInicial));
            }
            if (dataFinal != null) {
                spec = spec.and(porDataFinal(dataFinal));
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    // Filtro para compras realizadas após o fechamento da fatura
    public static Specification<Venda> porComprasAposFechamento(UUID clienteId, LocalDate fechamentoFatura) {
        return (root, query, criteriaBuilder) -> {
            if (clienteId == null || fechamentoFatura == null) {
                return criteriaBuilder.conjunction();
            }

            // Converte o dia de fechamento para LocalDateTime (considerando o início do dia)
            LocalDateTime inicioFechamento = fechamentoFatura.atStartOfDay();

            // Filtra por cliente e dataVenda após o fechamento
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("cliente").get("codigo"), clienteId),
                    criteriaBuilder.greaterThan(root.get("dataVenda"), inicioFechamento)
            );
        };
    }
}
