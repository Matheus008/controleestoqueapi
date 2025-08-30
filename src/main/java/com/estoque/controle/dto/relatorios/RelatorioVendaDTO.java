package com.estoque.controle.dto.relatorios;

import java.time.LocalDateTime;

public record RelatorioVendaDTO(
        Long id,
        String produtoNome,
        int quantidade,
        double valorTotalVendido,
        double valorDoProduto,
        String cliente,
        String usuario,
        LocalDateTime dataHora) {
}
