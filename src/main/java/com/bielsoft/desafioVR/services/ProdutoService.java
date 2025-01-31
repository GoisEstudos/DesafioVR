package com.bielsoft.desafioVR.services;

import com.bielsoft.desafioVR.dtos.ProdutoDto;
import com.bielsoft.desafioVR.entities.Produto;
import com.bielsoft.desafioVR.enuns.Status;
import com.bielsoft.desafioVR.exceptions.produtoException.ProdutoNotFoundException;
import com.bielsoft.desafioVR.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }


    public List<ProdutoDto> listarTodosProdutos() {
        return repository.findByStatus(Status.Ativo).stream().map(ProdutoDto::new).toList();
    }

    public ProdutoDto listarUmProduto(UUID id) {
        return repository.findByCodigoAndStatus(id, Status.Ativo).map(ProdutoDto::new)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado"));
    }

    public ProdutoDto cadastrarProduto(ProdutoDto dto) {
        Produto newProduto = new Produto();
        newProduto.setDescricao(dto.descricao());
        newProduto.setPreco(dto.preco());

        repository.save(newProduto);

        return new ProdutoDto(newProduto);
    }

    public ProdutoDto atualizarProduto(UUID codigo, ProdutoDto dto) {
        Produto newProduto = repository.findById(codigo)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado"));

        newProduto.setDescricao(dto.descricao());
        newProduto.setPreco(dto.preco());

        repository.save(newProduto);

        return new ProdutoDto(newProduto);
    }

    public void deletarProduto(UUID id) {
        Produto newProduto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado"));

        newProduto.setStatus(Status.Inativo);
        repository.save(newProduto);
    }

    public void ativarProduto(UUID id) {
        Produto newProduto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado"));

        newProduto.setStatus(Status.Ativo);
        repository.save(newProduto);
    }

}
