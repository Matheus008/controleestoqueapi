package com.estoque.controle.dto;

import com.estoque.controle.model.fornecedor.TipoFornecedor;

public record FornecedorDTO(String nomeFornecedor, String cpfOuCnpj, TipoFornecedor tipoFornecedor) {

    public static FornecedorDTO of(String nomeFornecedor, String cpfOuCnpj, TipoFornecedor tipoFornecedor) {
        if (cpfOuCnpj != null) {
            cpfOuCnpj = cpfOuCnpj.replaceAll("\\D", ""); // para ficar somente números
            if (cpfOuCnpj.length() == 11 && tipoFornecedor == TipoFornecedor.FISICA) {
                cpfOuCnpj = cpfOuCnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                        "$1.$2.$3-$4");
            } else if (cpfOuCnpj.length() == 14 && tipoFornecedor == TipoFornecedor.JURIDICA) {
                cpfOuCnpj = cpfOuCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                        "$1.$2.$3/$4-$5");
            } else {
                throw new RuntimeException("Tipo inválido");
            }
        }
        return new FornecedorDTO(nomeFornecedor, cpfOuCnpj, tipoFornecedor);
    }
}
