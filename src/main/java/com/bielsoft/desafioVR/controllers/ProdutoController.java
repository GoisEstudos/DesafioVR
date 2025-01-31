package com.bielsoft.desafioVR.controllers;

import com.bielsoft.desafioVR.dtos.ClienteDto;
import com.bielsoft.desafioVR.dtos.ProdutoDto;
import com.bielsoft.desafioVR.entities.Cliente;
import com.bielsoft.desafioVR.entities.Produto;
import com.bielsoft.desafioVR.services.ClienteService;
import com.bielsoft.desafioVR.services.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<ProdutoDto>> listarTodosProdutos() {
        return ResponseEntity.ok(service.listarTodosProdutos());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ProdutoDto> listarUmProdutos(@PathVariable UUID codigo) {
        return ResponseEntity.ok(service.listarUmProduto(codigo));
    }

    @PostMapping
    public ResponseEntity<ProdutoDto> cadastrarProduto(@RequestBody ProdutoDto dto) {
        ProdutoDto newProduto = service.cadastrarProduto(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(newProduto.codigo())
                .toUri();

        return ResponseEntity.created(location).body(newProduto);
    }

    @PatchMapping("/{codigo}")
    public ResponseEntity<ProdutoDto> atualizarProduto(@PathVariable UUID codigo, @RequestBody ProdutoDto dto) {
        return ResponseEntity.ok(service.atualizarProduto(codigo, dto));
    }

    @PatchMapping("/ativar/{codigo}")
    public ResponseEntity<Void> ativarProduto(@PathVariable UUID codigo) {
        service.ativarProduto(codigo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarProduto(@PathVariable UUID codigo) {
        service.deletarProduto(codigo);
        return ResponseEntity.noContent().build();
    }
}
