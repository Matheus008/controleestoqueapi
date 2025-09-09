package com.estoque.controle.exceptions;

public class ProdutoNaoEncontradoException extends RuntimeException{

    public ProdutoNaoEncontradoException() {
        super("Produto não encontrado!");
    }
}
