package com.estoque.controle.model.produto;

public record MovimentacaoDTO(int quantidade, String descricao, Long produtoId, TipoMovimentacao tipoMovimentacao) {
}
