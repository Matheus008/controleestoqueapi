package com.estoque.controle.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException{

    public UsuarioNaoEncontradoException(String email) {
        super("Usuário não encontrado "+ email);
    }
}
