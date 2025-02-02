package com.bielsoft.desafioVR.services;

import com.bielsoft.desafioVR.dtos.ClienteDto;
import com.bielsoft.desafioVR.dtos.ItemVendaDto;
import com.bielsoft.desafioVR.dtos.VendaDto;
import com.bielsoft.desafioVR.entities.Cliente;
import com.bielsoft.desafioVR.entities.ItemVenda;
import com.bielsoft.desafioVR.entities.Produto;
import com.bielsoft.desafioVR.entities.Venda;
import com.bielsoft.desafioVR.enuns.Status;
import com.bielsoft.desafioVR.exceptions.clienteException.ClienteNotFoundException;
import com.bielsoft.desafioVR.exceptions.produtoException.ProdutoNotFoundException;
import com.bielsoft.desafioVR.exceptions.vendaException.LimiteCreditoExcedidoException;
import com.bielsoft.desafioVR.exceptions.vendaException.ProdutoRepetidoException;
import com.bielsoft.desafioVR.exceptions.vendaException.VendaNotFoundException;
import com.bielsoft.desafioVR.repositories.ClienteRepository;
import com.bielsoft.desafioVR.repositories.ProdutoRepository;
import com.bielsoft.desafioVR.repositories.VendaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    public VendaService(VendaRepository repository, ProdutoRepository produtoRepository, ClienteRepository clienteRepository) {
        this.vendaRepository = repository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<VendaDto> listarTodasVendas() {
        return vendaRepository.findByStatus(Status.Ativo).stream().map(VendaDto::new).toList();
    }

    public VendaDto listarUmaVenda(UUID id) {
        return vendaRepository.findByCodigoAndStatus(id, Status.Ativo).map(VendaDto::new)
                .orElseThrow(() -> new VendaNotFoundException("Venda não encontrado"));
    }

    public List<VendaDto> listarVendaPorCliente(UUID id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));

        List<Venda> vendas = vendaRepository.findByClienteAndStatus(cliente, Status.Ativo);

        return vendas.stream()
                .map(venda -> {
                    double valorTotal = venda.getItens().stream()
                            .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                            .sum();

                    return new VendaDto(
                            venda.getCodigo(),
                            new ClienteDto(venda.getCliente()),
                            venda.getItens().stream().map(ItemVendaDto::new).toList(),
                            venda.getDataVenda(),
                            valorTotal // Adiciona o valorTotal ao DTO
                    );
                })
                .collect(Collectors.toList());
    }

    public List<VendaDto> filtrarVendas(UUID clienteId, UUID produtoId, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        Specification<Venda> spec = Specification
                .where(VendaSpecification.porCliente(clienteId))
                .and(VendaSpecification.porProduto(produtoId))
                .and(VendaSpecification.porPeriodo(dataInicial, dataFinal));

        List<Venda> vendas = vendaRepository.findAll(spec);

        return vendas.stream().map(VendaDto::new).toList();
    }

    public List<VendaDto> listarVendaPorProduto(UUID id) {
        // Busca o produto ou lança uma exceção se não encontrado
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado"));

        // Busca as vendas que contêm o produto e estão com status Ativo
        List<Venda> vendas = vendaRepository.findByProdutoAndStatus(produto, Status.Ativo);

        // Mapeia as vendas para VendaDto e calcula o valorTotal
        return vendas.stream()
                .map(venda -> {
                    double valorTotal = venda.getItens().stream()
                            .filter(item -> item.getProduto().equals(produto))
                            .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                            .sum();

                    return new VendaDto(
                            venda.getCodigo(),
                            new ClienteDto(venda.getCliente()),
                            venda.getItens().stream()
                                    .filter(item -> item.getProduto().equals(produto))
                                    .map(ItemVendaDto::new)
                                    .toList(),
                            venda.getDataVenda(),
                            valorTotal
                    );
                })
                .collect(Collectors.toList());
    }


    public VendaDto cadastrarVenda(VendaDto dto) {
        // 1. Busca o cliente pelo código (UUID)
        Cliente cliente = clienteRepository.findById(dto.cliente().codigo())
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado: " + dto.cliente().codigo()));

        // 2. Valida se há produtos repetidos na venda
        validarProdutosRepetidos(dto.itens());

        // 3. Calcula o valor total da venda
        Double valorTotalVenda = calcularValorTotalVenda(dto);

        // 4. Calcula o limite disponível considerando o ciclo de fechamento
        Double valorDisponivel = calcularLimiteDisponivelConsiderandoFechamento(cliente, dto.dataVenda());

        // 5. Valida se a venda excede o limite disponível
        if (valorTotalVenda > valorDisponivel) {
            LocalDate proximoFechamento = calcularProximoFechamento(cliente.getFechamentoFatura());
            throw new LimiteCreditoExcedidoException(
                    "Limite excedido! Disponível: " + valorDisponivel +
                            ". Próximo fechamento: " + proximoFechamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }

        // 6. Cria e salva a venda no banco de dados
        Venda newVenda = criarVenda(dto, cliente, valorTotalVenda);
        vendaRepository.save(newVenda);

        // 7. Atualiza o limite de crédito do cliente após a venda
        LocalDate proximoFechamento = calcularProximoFechamento(cliente.getFechamentoFatura());
        LocalDateTime proximoFechamentoDateTime = proximoFechamento.atStartOfDay();

        if (newVenda.getDataVenda().isBefore(proximoFechamentoDateTime)) {
            atualizarLimiteCreditoCliente(cliente);
        }

        return new VendaDto(newVenda);
    }

    public VendaDto atualizarVenda(UUID codigo, VendaDto dto) {

        Venda venda = vendaRepository.findById(codigo)
                .orElseThrow(() -> new VendaNotFoundException("Venda não encontrada"));

        Cliente cliente = clienteRepository.findById(dto.cliente().codigo())
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));

        List<ItemVenda> newItensVenda = dto.itens().stream()
                .map(itemDto -> {
                    Produto produto = produtoRepository.findById(itemDto.produto().codigo())
                            .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado"));

                    return new ItemVenda(null, produto, venda, itemDto.quantidade());
                })
                .toList();

        validarProdutosRepetidos(dto.itens());

        venda.setCliente(cliente);
        venda.getItens().clear();
        venda.getItens().addAll(newItensVenda);

        vendaRepository.save(venda);

        return new VendaDto(venda);
    }

    public void deletarVenda(UUID id) {
        Venda newVenda = vendaRepository.findById(id)
                .orElseThrow(() -> new VendaNotFoundException("Venda não encontrado"));

        newVenda.setStatus(Status.Inativo);
        vendaRepository.save(newVenda);
    }

    public void ativarVenda(UUID id) {
        Venda newVenda = vendaRepository.findById(id)
                .orElseThrow(() -> new VendaNotFoundException("Venda não encontrado"));

        newVenda.setStatus(Status.Ativo);
        vendaRepository.save(newVenda);
    }

    private Double calcularLimiteDisponivelConsiderandoFechamento(Cliente cliente,LocalDateTime dataVenda) {
        // 1. Encontra o ciclo correspondente à data da venda
        LocalDate fechamentoAnterior = encontrarFechamentoAnterior(cliente.getFechamentoFatura(), dataVenda.toLocalDate());
        LocalDate proximoFechamento = calcularProximoFechamento(fechamentoAnterior);

        // 2. Define o intervalo do ciclo
        LocalDateTime inicioCiclo = fechamentoAnterior.plusDays(1).atStartOfDay();
        LocalDateTime fimCiclo = proximoFechamento.atStartOfDay();

        // 3. Busca vendas dentro desse ciclo
        List<Venda> comprasCiclo = vendaRepository.findByClienteAndStatus(cliente, Status.Ativo).stream()
                .filter(venda -> venda.getDataVenda().isAfter(inicioCiclo) && venda.getDataVenda().isBefore(fimCiclo))
                .toList();

        // 4. Calcula o total gasto no ciclo
        Double totalGastoCiclo = comprasCiclo.stream()
                .mapToDouble(Venda::getValorTotal)
                .sum();

        return cliente.getLimiteCredito() - totalGastoCiclo;
    }

    private LocalDate encontrarFechamentoAnterior(LocalDate fechamentoBase, LocalDate dataVenda) {
        LocalDate fechamentoAnterior = fechamentoBase;
        while (fechamentoAnterior.isBefore(dataVenda)) {
            fechamentoAnterior = calcularProximoFechamento(fechamentoAnterior);
        }
        return fechamentoAnterior.minusMonths(1); // Volta para o fechamento anterior à data da venda
    }

    private LocalDate calcularProximoFechamento(LocalDate dataFechamentoAtual) {
        return dataFechamentoAtual.plusMonths(1)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    private Double calcularValorTotalVenda(VendaDto dto) {
        return dto.itens().stream()
                .mapToDouble(item -> item.produto().preco() * item.quantidade())
                .sum();
    }

    private Venda criarVenda(VendaDto dto, Cliente cliente, Double valorTotalVenda) {
        Venda newVenda = new Venda();
        newVenda.setCliente(cliente);
        newVenda.setDataVenda(dto.dataVenda());
        newVenda.setValorTotal(valorTotalVenda);

        // Cria os itens da venda
        List<ItemVenda> itensVenda = dto.itens().stream()
                .map(item -> {
                    Produto produto = produtoRepository.findById(item.produto().codigo())
                            .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado: " + item.produto().codigo()));
                    return new ItemVenda(null, produto, newVenda, item.quantidade());
                })
                .toList();

        newVenda.setItens(itensVenda);
        return newVenda;
    }

    private void atualizarLimiteCreditoCliente(Cliente cliente) {
        // Busca todas as compras do cliente
        List<Venda> comprasCliente = vendaRepository.findByClienteAndStatus(cliente, Status.Ativo);

        // Filtra compras realizadas após o fechamento da fatura
        Double totalGastoAposFechamento = comprasCliente.stream()
                .filter(venda -> venda.getDataVenda().isAfter(cliente.getFechamentoFatura().atStartOfDay()))
                .mapToDouble(Venda::getValorTotal)
                .sum();

        // Calcula o limite disponível
        double limiteDisponivel = cliente.getLimiteCredito() - totalGastoAposFechamento;

        // Verifica se o limite foi excedido
        if (limiteDisponivel < 0) {
            // Caso o limite tenha sido excedido, informa o valor disponível
            LocalDate proximoFechamento = calcularProximoFechamento(cliente.getFechamentoFatura());
            System.out.println("Limite excedido! Disponível: " + limiteDisponivel + ". Próximo fechamento: " + proximoFechamento);
        } else {
            // Caso o limite não tenha sido excedido
            System.out.println("Limite disponível: " + limiteDisponivel);
        }
    }


    private void validarProdutosRepetidos(List<ItemVendaDto> itens) {
        Set<String> codigosProdutos = new HashSet<>();
        for (ItemVendaDto item : itens) {
            String codigoProduto = String.valueOf(item.produto().codigo()); // UUID em formato String
            if (!codigosProdutos.add(codigoProduto)) {
                throw new ProdutoRepetidoException("Produto repetido: " + item.produto().descricao());
            }
        }
    }

}
