package com.bielsoft.desafioVR.services;

import com.bielsoft.desafioVR.dtos.ClienteDto;
import com.bielsoft.desafioVR.entities.Cliente;
import com.bielsoft.desafioVR.enuns.Status;
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
        return repository.findByStatus(Status.Ativo).stream().map(ClienteDto::new).toList();
    }

    public ClienteDto listarUmCliente(UUID id) {
        return repository.findByCodigoAndStatus(id, Status.Ativo).map(ClienteDto::new)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));
    }

    public ClienteDto cadastrarCliente(ClienteDto dto) {
        Cliente newCliente = new Cliente();
        newCliente.setNome(dto.nome());
        newCliente.setLimiteCredito(dto.limiteCredito());
        newCliente.setFechamentoFatura(dto.fechamentoFatura());

        repository.save(newCliente);

        return new ClienteDto(newCliente);
    }

    public ClienteDto atualizarCliente(UUID codigo, ClienteDto dto) {
        Cliente newCliente = repository.findById(codigo)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));

        newCliente.setNome(dto.nome());
        newCliente.setLimiteCredito(dto.limiteCredito());
        newCliente.setFechamentoFatura(dto.fechamentoFatura());

        repository.save(newCliente);

        return new ClienteDto(newCliente);
    }

    public void deletarCliente(UUID id) {
        Cliente newCliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));

        newCliente.setStatus(Status.Inativo);
        repository.save(newCliente);
    }

    public void ativarCliente(UUID id) {
        Cliente newCliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));

        newCliente.setStatus(Status.Ativo);
        repository.save(newCliente);
    }
}
