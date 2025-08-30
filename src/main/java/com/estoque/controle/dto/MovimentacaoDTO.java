package com.estoque.controle.dto;

import com.estoque.controle.model.produto.TipoMovimentacao;

public record MovimentacaoDTO(int quantidade, String descricao, Long produtoId, TipoMovimentacao tipoMovimentacao) {
}
