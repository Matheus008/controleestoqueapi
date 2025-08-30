package com.estoque.controle.model.relatorios;

import com.estoque.controle.model.produto.TipoMovimentacao;

import java.time.LocalDateTime;

public record RelatorioMovimentacaoDTO(
        Long id,
        String produtoNome,
        int quantidade,
        TipoMovimentacao tipoMovimentacao,
        String Descricao,
        String usuario,
        LocalDateTime dataHora
) {
}
