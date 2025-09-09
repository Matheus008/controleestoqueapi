package com.estoque.controle.exceptions;

public class FornecedorNaoEncontradoException extends RuntimeException{
    public FornecedorNaoEncontradoException() {
        super("Fornecedor não encontrado!");
    }
}
