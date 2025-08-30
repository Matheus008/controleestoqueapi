package com.estoque.controle.repository;

import com.estoque.controle.model.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeCliente(String nomeCliente);

    String findByCpfOuCnpj(String cpfOuCnpj);

}
