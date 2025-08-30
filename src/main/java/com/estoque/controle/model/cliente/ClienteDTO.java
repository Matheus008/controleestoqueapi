package com.estoque.controle.model.cliente;

public record ClienteDTO(String nomeCliente, String cpfOuCnpj, TipoCliente tipoCliente) {

    public static ClienteDTO of(String nomeCliente, String cpfOuCnpj, TipoCliente tipoCliente) {
        if (cpfOuCnpj != null) {
            cpfOuCnpj = cpfOuCnpj.replaceAll("\\D", ""); // para ficar somente números
            if (cpfOuCnpj.length() == 11 && tipoCliente == TipoCliente.FISICA) {
                cpfOuCnpj = cpfOuCnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                        "$1.$2.$3-$4");
            } else if (cpfOuCnpj.length() == 14 && tipoCliente == TipoCliente.JURIDICA) {
                cpfOuCnpj = cpfOuCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                        "$1.$2.$3/$4-$5");
            }
        }
        return new ClienteDTO(nomeCliente, cpfOuCnpj, tipoCliente);
    }
}
