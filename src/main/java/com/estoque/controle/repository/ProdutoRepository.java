package com.estoque.controle.repository;

import com.estoque.controle.dto.ProdutoRankingDTO;
import com.estoque.controle.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNome(String nome);

    @Query("SELECT v.produto.nome as produto, SUM(v.quantidade) as quantidadeVendida " +
            "FROM Venda v " +
            "WHERE v.dataVenda BETWEEN :inicio AND :fim " +
            "GROUP BY v.produto.nome " +
            "ORDER BY SUM(v.quantidade) DESC")
    List<ProdutoRankingDTO> rankingProdutos(@Param("inicio") LocalDateTime inicio,
                                            @Param("fim") LocalDateTime fim);
}
