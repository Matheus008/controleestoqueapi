package com.estoque.controle.dto.relatorios;

import com.estoque.controle.model.produto.TipoMovimentacao;

import java.time.LocalDateTime;

public record FiltroRelatorioDTO(
        LocalDateTime inicio,
        LocalDateTime fim,
        Long usuarioId,
        Long produtoId,
        Long clienteId,
        TipoMovimentacao tipoMovimentacao
) {}
