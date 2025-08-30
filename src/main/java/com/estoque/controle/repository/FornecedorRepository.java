package com.estoque.controle.repository;

import com.estoque.controle.model.cliente.Cliente;
import com.estoque.controle.model.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    List<Fornecedor> findByNomeFornecedor(String nomeCliente);

    String findByCpfOuCnpj(String cpfOuCnpj);

}