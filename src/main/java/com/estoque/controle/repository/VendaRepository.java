package com.estoque.controle.repository;

import com.estoque.controle.model.produto.Movimentacao;
import com.estoque.controle.model.vendas.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Venda> findByDataVendaAfter(LocalDateTime inicio);
    List<Venda> findByDataVendaBefore(LocalDateTime fim);
}
