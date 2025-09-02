package com.estoque.controle.repository;

import com.estoque.controle.dto.VendaIndicadoresDTO;
import com.estoque.controle.model.vendas.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Venda> findByDataVendaAfter(LocalDateTime inicio);
    List<Venda> findByDataVendaBefore(LocalDateTime fim);

    @Query("SELECT SUM(v.quantidade) as quantidadeTotalVendida, " +
            "SUM(v.valorTotalVendido) as faturamentoTotal " +
            "FROM Venda v " +
            "WHERE v.dataVenda BETWEEN :inicio AND :fim")
    VendaIndicadoresDTO calcularIndicadores(@Param("inicio")LocalDateTime inicio, @Param("fim") LocalDateTime fim);



}
