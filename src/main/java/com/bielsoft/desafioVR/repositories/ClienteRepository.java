package com.bielsoft.desafioVR.repositories;

import com.bielsoft.desafioVR.entities.Cliente;
import com.bielsoft.desafioVR.enuns.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    List<Cliente> findByStatus(Status status);
    Optional<Cliente> findByCodigoAndStatus(UUID id, Status status);
}
