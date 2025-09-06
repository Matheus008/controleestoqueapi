package com.estoque.controle.repository;

import com.estoque.controle.model.produto.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Movimentacao> findByDataHoraAfter(LocalDateTime inicio);
    List<Movimentacao> findByDataHoraBefore(LocalDateTime fim);
    List<Movimentacao> findByProdutoId(Long produtoId);

}
