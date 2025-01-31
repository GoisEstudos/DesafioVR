package com.bielsoft.desafioVR.repositories;

import com.bielsoft.desafioVR.entities.Produto;
import com.bielsoft.desafioVR.enuns.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    List<Produto> findByStatus(Status status);
    Optional<Produto> findByCodigoAndStatus(UUID id, Status status);
}
