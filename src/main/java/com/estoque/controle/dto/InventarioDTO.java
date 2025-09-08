package com.estoque.controle.dto;

import com.estoque.controle.model.produto.StatusEstoque;

public record InventarioDTO(String nomeProduto, int estoqueAtual, int estoqueMinimo, int estoqueMaximo, StatusEstoque statusEstoque) {
}
