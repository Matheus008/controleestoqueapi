package com.estoque.controle.exceptions;

public class ProdutoNaoEstaZeradoException extends RuntimeException{

    public ProdutoNaoEstaZeradoException() {
        super("O estoque do produto precisa estar zerado!");
    }
}
