package com.estoque.controle.repository;

import com.estoque.controle.model.produto.ConfigEstoque;
import com.estoque.controle.model.produto.StatusEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfigEstoqueRepository extends JpaRepository<ConfigEstoque, Long> {

    Optional<ConfigEstoque> findByProdutoId(Long produto_id);
}
