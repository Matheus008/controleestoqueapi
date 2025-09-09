package com.estoque.controle.exceptions;

public class ClienteNaoEncontradoException  extends RuntimeException{

    public ClienteNaoEncontradoException() {
        super("Cliente não encontrado!");
    }
}
