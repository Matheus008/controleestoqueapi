package com.estoque.controle.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException{

    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado");
    }
}
