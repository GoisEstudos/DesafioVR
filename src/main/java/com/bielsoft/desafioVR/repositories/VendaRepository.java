package com.bielsoft.desafioVR.repositories;

import com.bielsoft.desafioVR.entities.Cliente;
import com.bielsoft.desafioVR.entities.Produto;
import com.bielsoft.desafioVR.entities.Venda;
import com.bielsoft.desafioVR.enuns.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendaRepository extends JpaRepository<Venda, UUID>, JpaSpecificationExecutor<Venda> {
    List<Venda> findByStatus(Status status);

    Optional<Venda> findByCodigoAndStatus(UUID id, Status status);

    List<Venda> findByClienteAndStatus(Cliente cliente, Status status);

    @Query("SELECT DISTINCT v FROM Venda v " +
            "JOIN v.itens i " +
            "WHERE i.produto = :produto AND v.status = :status")
    List<Venda> findByProdutoAndStatus(Produto produto, Status status);
}

