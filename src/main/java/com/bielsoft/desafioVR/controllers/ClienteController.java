package com.bielsoft.desafioVR.controllers;


import com.bielsoft.desafioVR.dtos.ClienteDto;
import com.bielsoft.desafioVR.entities.Cliente;
import com.bielsoft.desafioVR.services.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> listarTodosClientes() {
        return ResponseEntity.ok(service.listarTodosClientes());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ClienteDto> listarUmCliente(@PathVariable UUID codigo) {
        return ResponseEntity.ok(service.listarUmCliente(codigo));
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody ClienteDto cliente) {
        Cliente newCliente = service.cadastrarCliente(cliente);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(newCliente.getCodigo())
                .toUri();

        return ResponseEntity.created(location).body(newCliente);
    }

    @PatchMapping("/{codigo}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable UUID codigo, @RequestBody ClienteDto cliente) {
        return ResponseEntity.ok(service.atualizarCliente(codigo,cliente));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletarCliente(@PathVariable UUID codigo) {
        service.deletarCliente(codigo);
        return ResponseEntity.noContent().build();
    }
}
