package com.bielsoft.desafioVR.services;

import com.bielsoft.desafioVR.dtos.ClienteDto;
import com.bielsoft.desafioVR.entities.Cliente;
import com.bielsoft.desafioVR.enuns.StatusCliente;
import com.bielsoft.desafioVR.exceptions.clienteException.ClienteNotFoundException;
import com.bielsoft.desafioVR.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<ClienteDto> listarTodosClientes() {
        return repository.findByStatus(StatusCliente.Ativo).stream().map(ClienteDto::new).toList();
    }

    public ClienteDto listarUmCliente(UUID id) {
        return repository.findByCodigoAndStatus(id, StatusCliente.Ativo).map(ClienteDto::new)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));
    }

    public Cliente cadastrarCliente(ClienteDto dto) {
        Cliente newCliente = new Cliente();
        newCliente.setNome(dto.nome());
        newCliente.setLimiteCredito(dto.limiteCredito());
        newCliente.setFechamentoFatura(dto.fechamentoFatura());

        return repository.save(newCliente);
    }

    public Cliente atualizarCliente(UUID codigo, ClienteDto cliente) {
        Cliente newCliente = repository.findById(codigo)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));

        newCliente.setNome(cliente.nome());
        newCliente.setLimiteCredito(cliente.limiteCredito());
        newCliente.setFechamentoFatura(cliente.fechamentoFatura());

        return repository.save(newCliente);
    }

    public void deletarCliente(UUID id) {
        Cliente newCliente = repository.findById(id).orElseThrow(RuntimeException::new);

        newCliente.setStatus(StatusCliente.Inativo);
        repository.save(newCliente);
    }

}
