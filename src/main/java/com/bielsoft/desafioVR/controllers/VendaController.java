package com.bielsoft.desafioVR.controllers;

import com.bielsoft.desafioVR.dtos.VendaDto;
import com.bielsoft.desafioVR.services.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VendaDto>> listarTodasVendas() {
        return ResponseEntity.ok(service.listarTodasVendas());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<VendaDto> listarUmaVenda(@PathVariable UUID codigo) {
        return ResponseEntity.ok(service.listarUmaVenda(codigo));
    }

    @GetMapping("/{codigo}/por-cliente")
    public ResponseEntity<List<VendaDto>> listarVendaPorCliente(@PathVariable UUID codigo) {
        return ResponseEntity.ok(service.listarVendaPorCliente(codigo));
    }
    @GetMapping("/{codigo}/por-produto")
    public ResponseEntity<List<VendaDto>> listarVendaPorProduto(@PathVariable UUID codigo) {
        return ResponseEntity.ok(service.listarVendaPorProduto(codigo));
    }

    @GetMapping("/por-filtro")
    public ResponseEntity<List<VendaDto>> listarVendaPorFiltro(UUID clienteId, UUID produtoId, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return ResponseEntity.ok(service.filtrarVendas(clienteId, produtoId, dataInicial, dataFinal));
    }

    @PostMapping
    public ResponseEntity<VendaDto> cadastrarVenda(@RequestBody @Valid VendaDto dto) {
        VendaDto newVenda = service.cadastrarVenda(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(newVenda.codigo())
                .toUri();

        return ResponseEntity.created(location).body(newVenda);
    }

    @PatchMapping("/{codigo}")
    public ResponseEntity<VendaDto> atualizarVenda(@PathVariable @Valid UUID codigo, @RequestBody VendaDto cliente) {
        return ResponseEntity.ok(service.atualizarVenda(codigo,cliente));
    }

    @PatchMapping("/ativar/{codigo}")
    public ResponseEntity<Void> ativarVenda(@PathVariable UUID codigo) {
        service.ativarVenda(codigo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarVenda(@PathVariable UUID codigo) {
        service.deletarVenda(codigo);
        return ResponseEntity.noContent().build();
    }
}
